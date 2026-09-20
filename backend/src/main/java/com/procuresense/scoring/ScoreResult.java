package com.procuresense.scoring;

import lombok.*;

import java.util.*;

/**
 * SCORE RESULT - Holds all 8-factor scores and final weighted score
 * 
 * Used for:
 * 1. Storing scores in database
 * 2. Ranking suppliers
 * 3. Generating explanations
 * 4. Displaying in UI
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreResult {
    
    // Individual factor scores (0-100)
    private Double costScore;
    private Double qualityScore;
    private Double deliveryScore;
    private Double reliabilityScore;
    private Double capacityScore;
    private Double paymentScore;
    private Double riskScore;
    private Double dependencyScore;
    
    // Weighted contributions (factor_score × weight)
    private Double costContribution;
    private Double qualityContribution;
    private Double deliveryContribution;
    private Double reliabilityContribution;
    private Double capacityContribution;
    private Double paymentContribution;
    private Double riskContribution;
    private Double dependencyContribution;
    
    // Final score
    private Double finalWeightedScore; // Sum of all contributions
    
    // Risk flags
    private List<String> riskFlags = new ArrayList<>();
    
    /**
     * Calculate final score from contributions
     */
    public void calculateFinalScore() {
        finalWeightedScore = costContribution + qualityContribution + deliveryContribution
                           + reliabilityContribution + capacityContribution + paymentContribution
                           + riskContribution + dependencyContribution;
    }
    
    /**
     * Get all factor scores as map (useful for UI)
     */
    public Map<String, Double> getFactorScoresMap() {
        Map<String, Double> scores = new LinkedHashMap<>();
        scores.put("cost", costScore);
        scores.put("quality", qualityScore);
        scores.put("delivery", deliveryScore);
        scores.put("reliability", reliabilityScore);
        scores.put("capacity", capacityScore);
        scores.put("payment", paymentScore);
        scores.put("risk", riskScore);
        scores.put("dependency", dependencyScore);
        return scores;
    }
    
    /**
     * Get all contributions as map
     */
    public Map<String, Double> getContributionsMap() {
        Map<String, Double> contributions = new LinkedHashMap<>();
        contributions.put("cost", costContribution);
        contributions.put("quality", qualityContribution);
        contributions.put("delivery", deliveryContribution);
        contributions.put("reliability", reliabilityContribution);
        contributions.put("capacity", capacityContribution);
        contributions.put("payment", paymentContribution);
        contributions.put("risk", riskContribution);
        contributions.put("dependency", dependencyContribution);
        return contributions;
    }
}
