package com.procuresense.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuotationItemDTO {

    private Long id;
    private Long quantity;
    private BigDecimal unitPriceInr;
    private BigDecimal totalPriceInr;
    private String originalCurrency;
    private Integer deliveryDays;
    private String paymentTerms;
    private Long supplierCapacity;
    private String qualityCertifications;
    private Double quantityConfidence;
    private Double priceConfidence;
    private Double deliveryConfidence;
    private Boolean manuallyEdited;
}