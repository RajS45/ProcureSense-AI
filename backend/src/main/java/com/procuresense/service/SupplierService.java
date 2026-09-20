package com.procuresense.service;

import com.procuresense.dto.SupplierDTO;
import com.procuresense.dto.SupplierDetailDTO;
import com.procuresense.model.Supplier;
import com.procuresense.model.SupplierStatus;
import com.procuresense.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public List<SupplierDTO> getAllSuppliers() {

        return supplierRepository
                .findByStatus(SupplierStatus.ACTIVE)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public SupplierDetailDTO getSupplierById(Long id) {

        Supplier supplier =
                supplierRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Supplier not found"));

        return toDetailDTO(supplier);
    }

    public Supplier getBySupplierIdOrThrow(String supplierId) {

        return supplierRepository
                .findBySupplierId(supplierId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Supplier not found: " + supplierId));
    }

    public void createDemoSuppliers() {

        if (supplierRepository.count() > 0) {
            log.info(
                    "Suppliers already exist, skipping demo creation");
            return;
        }

        Supplier supplierA = Supplier.builder()
                .supplierId("SUP-001")
                .name("ABC Steel Pvt Ltd")
                .location("India")
                .status(SupplierStatus.ACTIVE)
                .classification("PREFERRED")
                .onTimeDeliveryRate(85.0)
                .qualityScore(75.0)
                .capacityRating(85.0)
                .qualityCertifications("ISO 9001")
                .financialHealthScore(80.0)
                .geopoliticalRisk("LOW")
                .purchaseConcentration(8.0)
                .build();

        supplierRepository.save(supplierA);

        Supplier supplierB = Supplier.builder()
                .supplierId("SUP-002")
                .name("XYZ Components Ltd")
                .location("Singapore")
                .status(SupplierStatus.ACTIVE)
                .classification("PREFERRED")
                .onTimeDeliveryRate(96.0)
                .qualityScore(90.0)
                .capacityRating(90.0)
                .qualityCertifications("ISO 9001, ISO 14001")
                .financialHealthScore(85.0)
                .geopoliticalRisk("LOW")
                .purchaseConcentration(5.0)
                .build();

        supplierRepository.save(supplierB);

        Supplier supplierC = Supplier.builder()
                .supplierId("SUP-003")
                .name("LMN Supplies India")
                .location("India")
                .status(SupplierStatus.ACTIVE)
                .classification("SECONDARY")
                .onTimeDeliveryRate(80.0)
                .qualityScore(80.0)
                .capacityRating(65.0)
                .qualityCertifications("ISO 9001")
                .financialHealthScore(75.0)
                .geopoliticalRisk("LOW")
                .purchaseConcentration(3.0)
                .build();

        supplierRepository.save(supplierC);

        log.info("Created demo suppliers");
    }

    private SupplierDTO toDTO(Supplier supplier) {

        return new SupplierDTO(
                supplier.getId(),
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getLocation(),
                supplier.getStatus().name(),
                supplier.getOnTimeDeliveryRate(),
                supplier.getQualityScore()
        );
    }

    private SupplierDetailDTO toDetailDTO(Supplier supplier) {

        SupplierDetailDTO dto = new SupplierDetailDTO();

        dto.setId(supplier.getId());
        dto.setSupplierId(supplier.getSupplierId());
        dto.setName(supplier.getName());
        dto.setLocation(supplier.getLocation());
        dto.setStatus(supplier.getStatus().name());
        dto.setOnTimeDeliveryRate(
                supplier.getOnTimeDeliveryRate());
        dto.setQualityScore(supplier.getQualityScore());
        dto.setFinancialHealthScore(
                supplier.getFinancialHealthScore());
        dto.setGeopoliticalRisk(
                supplier.getGeopoliticalRisk());
        dto.setPurchaseConcentration(
                supplier.getPurchaseConcentration());
        dto.setQualityCertifications(
                supplier.getQualityCertifications());

        return dto;
    }
}