package com.procuresense.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.procuresense.dto.QuotationDTO;
import com.procuresense.dto.QuotationItemDTO;
import com.procuresense.model.Quotation;
import com.procuresense.model.QuotationItem;
import com.procuresense.model.QuotationStatus;
import com.procuresense.model.PurchaseRequest;
import com.procuresense.model.Supplier;
import com.procuresense.model.User;
import com.procuresense.repository.PurchaseRequestRepository;
import com.procuresense.repository.QuotationItemRepository;
import com.procuresense.repository.QuotationRepository;
import com.procuresense.repository.SupplierRepository;
import com.procuresense.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuotationService {

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private QuotationItemRepository quotationItemRepository;

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AiExtractionService aiExtractionService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public QuotationDTO createQuotation(Long requestId, Long supplierId, String fileName, Long userId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Supplier supplier = null;
        if (supplierId != null) {
            supplier = supplierRepository.findById(supplierId).orElse(null);
        }

        Quotation quotation = Quotation.builder()
                .purchaseRequest(request)
                .supplier(supplier)
                .fileName(fileName)
                .filePath("/uploads/" + fileName)
                .status(QuotationStatus.PENDING)
                .uploadedBy(user)
                .build();

        Quotation saved = quotationRepository.save(quotation);
        log.info("Created quotation: {} for request: {}", saved.getId(), request.getRequestCode());

        return toDTO(saved);
    }

    public QuotationDTO getQuotation(Long id) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quotation not found"));
        return toDTO(quotation);
    }

    public List<QuotationDTO> getQuotationsForRequest(Long requestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));

        return quotationRepository.findByPurchaseRequest(request)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void extractQuotation(Long quotationId) {
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new RuntimeException("Quotation not found"));

        quotation.setStatus(QuotationStatus.PROCESSING);
        quotationRepository.save(quotation);

        try {
            AiExtractionService.ExtractionResponse response = aiExtractionService
                    .extractQuotation(quotation.getFileName(), quotation.getFilePath());

            if (response.getStatus().equals("SUCCESS")) {
                quotation.setExtractedJson(response.getExtractedDataJson());
                quotation.setExtractionConfidence(response.getConfidence());
                quotation.setStatus(QuotationStatus.EXTRACTED);

                try {
                    Map<String, Object> extracted = objectMapper.readValue(
                            response.getExtractedDataJson(), Map.class);
                    String supplierName = (String) extracted.get("supplier_name");

                    if (supplierName != null && quotation.getSupplier() == null) {
                        Optional<Supplier> supplier = supplierRepository.findAll()
                                .stream()
                                .filter(s -> s.getName().equalsIgnoreCase(supplierName))
                                .findFirst();
                        supplier.ifPresent(quotation::setSupplier);
                    }
                } catch (Exception e) {
                    log.debug("Could not parse supplier name from extraction");
                }

                createQuotationItemFromExtraction(
                        quotation,
                        response.getExtractedDataJson()
                );

            } else {
                quotation.setStatus(QuotationStatus.MANUAL_REVIEW);
                quotation.setExtractionConfidence(0.0);
                log.warn("Extraction failed for quotation: {}", quotationId);
            }

        } catch (Exception e) {
            quotation.setStatus(QuotationStatus.FAILED);
            log.error("Error extracting quotation: {}", quotationId, e);
        }

        quotationRepository.save(quotation);
    }

    private void createQuotationItemFromExtraction(
            Quotation quotation,
            String extractedJson) {

        try {
            Map<String, Object> data =
                    objectMapper.readValue(extractedJson, Map.class);

            QuotationItem item = QuotationItem.builder()
                    .quotation(quotation)
                    .quantity(getLongValue(data, "quantity"))
                    .unitPriceInr(getBigDecimalValue(data, "unit_price"))
                    .totalPriceInr(getBigDecimalValue(data, "total_price"))
                    .originalCurrency((String) data.get("currency"))
                    .originalUnit((String) data.get("unit"))
                    .deliveryDays(getIntValue(data, "delivery_days"))
                    .paymentTerms((String) data.get("payment_terms"))
                    .paymentTermsDays(getIntValue(data, "payment_terms_days"))
                    .supplierCapacity(getLongValue(data, "capacity"))
                    .qualityCertifications((String) data.get("quality_certification"))
                    .quantityConfidence(getDoubleValue(data, "quantity_confidence"))
                    .priceConfidence(getDoubleValue(data, "unit_price_confidence"))
                    .deliveryConfidence(getDoubleValue(data, "delivery_days_confidence"))
                    .manuallyEdited(false)
                    .build();

            quotationItemRepository.save(item);
            log.info("Created quotation item for quotation: {}", quotation.getId());

        } catch (Exception e) {
            log.error("Error creating quotation item from extraction", e);
        }
    }

    private Long getLongValue(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val instanceof Number) {
            return ((Number) val).longValue();
        }
        return null;
    }

    private Integer getIntValue(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val instanceof Number) {
            return ((Number) val).intValue();
        }
        return null;
    }

    private Double getDoubleValue(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        }
        return null;
    }

    private BigDecimal getBigDecimalValue(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val instanceof Number) {
            return BigDecimal.valueOf(((Number) val).doubleValue());
        }
        return null;
    }

    private QuotationDTO toDTO(Quotation quotation) {
        QuotationItemDTO itemDTO = null;

        Optional<QuotationItem> item =
                quotationItemRepository.findByQuotation(quotation);

        if (item.isPresent()) {
            itemDTO = quotationItemToDTO(item.get());
        }

        return new QuotationDTO(
                quotation.getId(),
                quotation.getPurchaseRequest().getId(),
                quotation.getSupplier() != null
                        ? quotation.getSupplier().getId()
                        : null,
                quotation.getSupplier() != null
                        ? quotation.getSupplier().getName()
                        : "Unknown",
                quotation.getStatus().name(),
                quotation.getExtractionConfidence(),
                itemDTO,
                quotation.getUploadedAt()
        );
    }

    private QuotationItemDTO quotationItemToDTO(QuotationItem item) {
        return new QuotationItemDTO(
                item.getId(),
                item.getQuantity(),
                item.getUnitPriceInr(),
                item.getTotalPriceInr(),
                item.getOriginalCurrency(),
                item.getDeliveryDays(),
                item.getPaymentTerms(),
                item.getSupplierCapacity(),
                item.getQualityCertifications(),
                item.getQuantityConfidence(),
                item.getPriceConfidence(),
                item.getDeliveryConfidence(),
                item.getManuallyEdited()
        );
    }
}
