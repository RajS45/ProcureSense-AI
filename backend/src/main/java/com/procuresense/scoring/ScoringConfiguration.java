package com.procuresense.scoring;

import lombok.*;

/**
 * SCORING CONFIGURATION - 8 FACTORS
 * 
 * All factors must total 100% (1.0).
 * This ensures deterministic, reproducible scoring.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoringConfiguration {
    
    // Factor weights (default configuration)
    private Double costWeight = 0.20;           // 20%
    private Double qualityWeight = 0.15;        // 15%
    private Double deliveryWeight = 0.15;       // 15%
    private Double reliabilityWeight = 0.15;    // 15%
    private Double capacityWeight = 0.10;       // 10%
    private Double paymentWeight = 0.10;        // 10%
    private Double riskWeight = 0.10;           // 10%
    private Double dependencyWeight = 0.05;     // 5%
    
    /**
     * Validate that weights sum to 1.0 (100%)
     */
    public boolean isValid() {
        double sum = costWeight + qualityWeight + deliveryWeight + reliabilityWeight
                   + capacityWeight + paymentWeight + riskWeight + dependencyWeight;
        // Allow small floating point errors (within 0.001)
        return Math.abs(sum - 1.0) < 0.001;
    }
    
    /**
     * Get total weight for validation
     */
    public double getTotalWeight() {
        return costWeight + qualityWeight + deliveryWeight + reliabilityWeight
             + capacityWeight + paymentWeight + riskWeight + dependencyWeight;
    }
}
