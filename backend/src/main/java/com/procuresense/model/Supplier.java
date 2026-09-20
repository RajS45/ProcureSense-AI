package com.procuresense.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String supplierId;

    @Column(nullable = false)
    private String name;

    private String location;

    @Enumerated(EnumType.STRING)
    private SupplierStatus status = SupplierStatus.ACTIVE;

    private String classification; // STRATEGIC, PREFERRED, SECONDARY

    // Performance metrics
    private Double onTimeDeliveryRate = 80.0; // percentage
    private Double qualityScore = 75.0; // 0-100
    private Double capacityRating = 80.0; // 0-100
    private String qualityCertifications; // ISO 9001, etc.

    // Risk indicators
    private Double financialHealthScore = 80.0; // 0-100
    private String geopoliticalRisk; // LOW, MEDIUM, HIGH
    private String riskNotes;

    // Dependency
    private Double purchaseConcentration = 0.0; // percentage

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

