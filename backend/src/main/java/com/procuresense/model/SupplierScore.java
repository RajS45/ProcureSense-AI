package com.procuresense.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private PurchaseRequest purchaseRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    // 8-Factor scores (0-100 each)
    private Double costScore;
    private Double qualityScore;
    private Double deliveryScore;
    private Double reliabilityScore;
    private Double capacityScore;
    private Double paymentScore;
    private Double riskScore;
    private Double dependencyScore;

    // Weighted contributions
    private Double costContribution;
    private Double qualityContribution;
    private Double deliveryContribution;
    private Double reliabilityContribution;
    private Double capacityContribution;
    private Double paymentContribution;
    private Double riskContribution;
    private Double dependencyContribution;

    // Final score
    private Double finalWeightedScore; // 0-100
    private Integer ranking; // 1, 2, 3, etc.

    // Risk flags
    @Column(columnDefinition = "TEXT")
    private String riskFlags; // JSON array of risk flags

    private LocalDateTime calculatedAt;
    private String scoringVersion; // For audit trail

    @PrePersist
    protected void onCreate() {
        calculatedAt = LocalDateTime.now();
    }
}
