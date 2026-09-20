package com.procuresense.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String requestCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long requestedQuantity;

    @Column(nullable = false)
    private LocalDate requiredByDate;

    private BigDecimal budget;

    private Integer currentInventory = 0;
    private Integer inventoryDays = 0; // How many days of inventory on hand

    @Enumerated(EnumType.STRING)
    private BusinessPriority businessPriority = BusinessPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String notes;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}


