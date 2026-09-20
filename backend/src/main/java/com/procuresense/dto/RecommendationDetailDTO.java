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
public class RecommendationDetailDTO extends RecommendationDTO {

    private SupplierScoreDTO preferredScoreDetails;
    private Long alternativeSupplierId;
    private String alternativeSupplierName;
    private Double alternativeScore;
    private String alternativeReasoning;
    private String riskAssessment;
    private List<String> riskFlags;
}