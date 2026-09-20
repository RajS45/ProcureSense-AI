package com.procuresense.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private PurchaseRequest purchaseRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preferred_supplier_id", nullable = false)
    private Supplier preferredSupplier;

    private Double preferredScore;

    // Alternative supplier (for disruption scenario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternative_supplier_id")
    private Supplier alternativeSupplier;

    private Double alternativeScore;

    // AI-generated explanations
    @Column(columnDefinition = "TEXT")
    private String reasoning; // Why was preferred chosen?

    @Column(columnDefinition = "TEXT")
    private String alternativeReasoning;

    @Column(columnDefinition = "TEXT")
    private String riskAssessment;

    @Enumerated(EnumType.STRING)
    private RecommendationStatus status = RecommendationStatus.DRAFT;

    private String disruptionScenario; // "UNAVAILABLE", "HIGH_RISK", etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

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

