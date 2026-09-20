package com.procuresense.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quotations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private PurchaseRequest purchaseRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private String filePath; // Path to uploaded PDF
    private String fileName;

    @Enumerated(EnumType.STRING)
    private QuotationStatus status = QuotationStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String extractedJson; // AI extraction result as JSON

    private Double extractionConfidence = 0.0; // 0-100

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    private LocalDateTime uploadedAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

