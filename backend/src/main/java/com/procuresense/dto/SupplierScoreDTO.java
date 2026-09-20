package com.procuresense.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierScoreDTO {

    private Long id;
    private Long supplierId;
    private String supplierName;

    private Double costScore;
    private Double qualityScore;
    private Double deliveryScore;
    private Double reliabilityScore;
    private Double capacityScore;
    private Double paymentScore;
    private Double riskScore;
    private Double dependencyScore;

    private Double finalWeightedScore;
    private Integer ranking;

    private List<String> riskFlags;
}