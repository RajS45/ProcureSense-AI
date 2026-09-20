package com.procuresense.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "quotation_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id", nullable = false)
    private Quotation quotation;

    // Normalized fields (internal standard units)
    private Long quantity;
    private BigDecimal unitPriceInr; // All normalized to INR
    private BigDecimal totalPriceInr;

    // Original fields (for reference)
    private String originalCurrency;
    private String originalUnit; // e.g., "per 100 units"
    private String paymentTerms; // e.g., "Net 30"
    private Integer paymentTermsDays;

    // Delivery information
    private Integer deliveryDays;
    private String deliveryDate;
    private String incoterms; // FOB, CIF, etc.

    // Capacity & quality
    private Long supplierCapacity;
    private String qualityCertifications; // ISO 9001, etc.

    // Confidence scores
    private Double quantityConfidence = 0.0;
    private Double priceConfidence = 0.0;
    private Double deliveryConfidence = 0.0;

    // Notes
    private String notes;

    // Manual override flag
    private Boolean manuallyEdited = false;
}
