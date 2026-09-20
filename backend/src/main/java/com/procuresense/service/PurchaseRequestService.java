package com.procuresense.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.procuresense.dto.*;
import com.procuresense.model.*;
import com.procuresense.repository.*;
import com.procuresense.scoring.ScoreResult;
import com.procuresense.scoring.ScoringConfiguration;
import com.procuresense.scoring.ScoringEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PurchaseRequestService {

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create new purchase requirement
     */
    @Transactional
    public PurchaseRequestDTO createPurchaseRequest(CreatePurchaseRequestDTO dto, Long userId) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String requestCode = "REQ-" + System.currentTimeMillis();

        PurchaseRequest request = PurchaseRequest.builder()
                .requestCode(requestCode)
                .product(product)
                .requestedQuantity(dto.getQuantity())
                .requiredByDate(dto.getRequiredByDate())
                .budget(dto.getBudget())
                .currentInventory(dto.getCurrentInventory() != null ? dto.getCurrentInventory() : 0)
                .businessPriority(BusinessPriority.valueOf(dto.getPriority()))
                .status(RequestStatus.DRAFT)
                .createdBy(user)
                .notes(dto.getNotes())
                .build();

        // Calculate inventory days
        if (dto.getCurrentInventory() != null && dto.getCurrentInventory() > 0) {
            // Assume consumption of 100 units per day (simplified)
            request.setInventoryDays(dto.getCurrentInventory() / 100);
        }

        PurchaseRequest saved = purchaseRequestRepository.save(request);
        log.info("Created purchase request: {}", saved.getRequestCode());

        return toDTO(saved);
    }

    /**
     * Get purchase request
     */
    public PurchaseRequestDTO getPurchaseRequest(Long id) {
        PurchaseRequest request = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));
        return toDTO(request);
    }

    /**
     * Get all purchase requests
     */
    public List<PurchaseRequestDTO> getAllPurchaseRequests() {
        return purchaseRequestRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update request status
     */
    @Transactional
    public void updateStatus(Long requestId, RequestStatus status) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));
        request.setStatus(status);
        purchaseRequestRepository.save(request);
        log.info("Updated request {} status to {}", requestId, status);
    }

    /**
     * Create demo product
     */
    public void createDemoProduct() {
        if (productRepository.count() > 0) {
            return;
        }

        Product product = Product.builder()
                .productCode("PROD-STEEL-001")
                .productName("Steel Coils (1 MT)")
                .category("Raw Materials")
                .unitOfMeasure("pieces")
                .build();
        productRepository.save(product);
        log.info("Created demo product");
    }

    private PurchaseRequestDTO toDTO(PurchaseRequest request) {
        return new PurchaseRequestDTO(
                request.getId(),
                request.getRequestCode(),
                request.getProduct().getId(),
                request.getProduct().getProductName(),
                request.getRequestedQuantity(),
                request.getRequiredByDate(),
                request.getBudget(),
                request.getInventoryDays(),
                request.getBusinessPriority().name(),
                request.getStatus().name(),
                request.getCreatedAt()
        );
    }
}
