package com.procuresense.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {

    private Long id;
    private Long requestId;
    private Long preferredSupplierId;
    private String preferredSupplierName;
    private Double preferredScore;
    private String reasoning;
    private String status;
}