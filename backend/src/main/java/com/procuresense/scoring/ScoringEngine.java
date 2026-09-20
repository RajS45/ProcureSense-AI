package com.procuresense.scoring;

import com.procuresense.model.PurchaseRequest;
import com.procuresense.model.QuotationItem;
import com.procuresense.scoring.ScoringConfiguration;
import com.procuresense.scoring.ScoreResult;
import com.procuresense.model.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * SCORING ENGINE - Deterministic 8-Factor Supplier Evaluation
 *
 * Calculates supplier scores using deterministic business rules.
 */
@Slf4j
@Component
public class ScoringEngine {

    /**
     * Calculate complete score for a supplier.
     */
    public ScoreResult calculateScore(
            PurchaseRequest request,
            Supplier supplier,
            QuotationItem quotation,
            ScoringConfiguration config) {

        if (request == null) {
            throw new IllegalArgumentException("Purchase request cannot be null");
        }

        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }

        if (quotation == null) {
            throw new IllegalArgumentException("Quotation item cannot be null");
        }

        if (config == null) {
            config = new ScoringConfiguration();
        }

        log.info(
                "Calculating score for supplier: {} for request: {}",
                supplier.getName(),
                request.getRequestCode()
        );

        ScoreResult result = new ScoreResult();

        // 8 factor scores
        result.setCostScore(calculateCostScore(quotation, request));
        result.setQualityScore(calculateQualityScore(supplier));
        result.setDeliveryScore(calculateDeliveryScore(quotation, request));
        result.setReliabilityScore(calculateReliabilityScore(supplier));
        result.setCapacityScore(calculateCapacityScore(quotation, request));
        result.setPaymentScore(calculatePaymentScore(quotation));
        result.setRiskScore(calculateRiskScore(supplier));
        result.setDependencyScore(calculateDependencyScore(supplier));

        // Weighted contributions
        result.setCostContribution(
                result.getCostScore() * config.getCostWeight()
        );

        result.setQualityContribution(
                result.getQualityScore() * config.getQualityWeight()
        );

        result.setDeliveryContribution(
                result.getDeliveryScore() * config.getDeliveryWeight()
        );

        result.setReliabilityContribution(
                result.getReliabilityScore() * config.getReliabilityWeight()
        );

        result.setCapacityContribution(
                result.getCapacityScore() * config.getCapacityWeight()
        );

        result.setPaymentContribution(
                result.getPaymentScore() * config.getPaymentWeight()
        );

        result.setRiskContribution(
                result.getRiskScore() * config.getRiskWeight()
        );

        result.setDependencyContribution(
                result.getDependencyScore() * config.getDependencyWeight()
        );

        // Calculate final score
        result.calculateFinalScore();

        // Risk flags
        identifyRiskFlags(
                result,
                request,
                supplier,
                quotation
        );

        log.info(
                "Final score for {} = {}",
                supplier.getName(),
                result.getFinalWeightedScore()
        );

        return result;
    }

    /**
     * FACTOR 1: COST
     */
    private Double calculateCostScore(
            QuotationItem quotation,
            PurchaseRequest request) {

        if (quotation.getTotalPriceInr() == null) {
            return 0.0;
        }

        if (request.getBudget() == null) {
            return 75.0;
        }

        double quotedTotal
                = quotation.getTotalPriceInr().doubleValue();

        double budget
                = request.getBudget().doubleValue();

        if (budget <= 0) {
            return 75.0;
        }

        if (quotedTotal <= budget * 0.80) {
            return 100.0;
        }

        if (quotedTotal <= budget) {
            return 85.0;
        }

        if (quotedTotal <= budget * 1.20) {
            return 60.0;
        }

        return 30.0;
    }

    /**
     * FACTOR 2: QUALITY
     */
    private Double calculateQualityScore(Supplier supplier) {

        if (supplier.getQualityScore() != null) {
            return clamp(
                    supplier.getQualityScore(),
                    0.0,
                    100.0
            );
        }

        return 75.0;
    }

    /**
     * FACTOR 3: DELIVERY
     */
    private Double calculateDeliveryScore(
            QuotationItem quotation,
            PurchaseRequest request) {

        if (quotation.getDeliveryDays() == null) {
            return 0.0;
        }

        LocalDate requiredDate
                = request.getRequiredByDate();

        if (requiredDate == null) {
            return 75.0;
        }

        LocalDate quotedDate
                = LocalDate.now()
                        .plusDays(quotation.getDeliveryDays());

        long daysMargin
                = ChronoUnit.DAYS.between(
                        quotedDate,
                        requiredDate
                );

        if (daysMargin >= 2) {
            return 100.0;
        }

        if (daysMargin >= 0) {
            return 80.0;
        }

        return 0.0;
    }

    /**
     * FACTOR 4: RELIABILITY
     */
    private Double calculateReliabilityScore(
            Supplier supplier) {

        if (supplier.getOnTimeDeliveryRate() != null) {
            return clamp(
                    supplier.getOnTimeDeliveryRate(),
                    0.0,
                    100.0
            );
        }

        return 70.0;
    }

    /**
     * FACTOR 5: CAPACITY
     */
    private Double calculateCapacityScore(
            QuotationItem quotation,
            PurchaseRequest request) {

        if (quotation.getSupplierCapacity() == null) {
            return 75.0;
        }

        long required
                = request.getRequestedQuantity();

        long available
                = quotation.getSupplierCapacity();

        if (required <= 0) {
            return 75.0;
        }

        if (available >= required) {
            return 100.0;
        }

        if (available >= required * 0.80) {
            return 85.0;
        }

        if (available >= required * 0.50) {
            return 60.0;
        }

        return 0.0;
    }

    /**
     * FACTOR 6: PAYMENT TERMS
     */
    private Double calculatePaymentScore(
            QuotationItem quotation) {

        if (quotation.getPaymentTermsDays() == null) {
            return 75.0;
        }

        int days
                = quotation.getPaymentTermsDays();

        if (days >= 30 && days <= 60) {
            return 100.0;
        }

        if (days >= 15) {
            return 90.0;
        }

        if (days >= 0) {
            return 80.0;
        }

        return 50.0;
    }

    /**
     * FACTOR 7: SUPPLIER RISK
     */
    private Double calculateRiskScore(
            Supplier supplier) {

        double riskScore = 85.0;

        if (supplier.getFinancialHealthScore() != null) {

            double financialHealth
                    = supplier.getFinancialHealthScore();

            if (financialHealth < 50) {
                riskScore -= 30;
            } else if (financialHealth < 70) {
                riskScore -= 15;
            }
        }

        if (supplier.getGeopoliticalRisk() != null) {

            String risk
                    = supplier.getGeopoliticalRisk()
                            .trim()
                            .toUpperCase();

            switch (risk) {

                case "HIGH":
                    riskScore -= 25;
                    break;

                case "MEDIUM":
                    riskScore -= 10;
                    break;

                case "LOW":
                    break;

                default:
                    break;
            }
        }

        return clamp(
                riskScore,
                0.0,
                100.0
        );
    }

    /**
     * FACTOR 8: DEPENDENCY
     */
    private Double calculateDependencyScore(
            Supplier supplier) {

        Double concentration
                = supplier.getPurchaseConcentration();

        if (concentration == null) {
            return 85.0;
        }

        if (concentration <= 5.0) {
            return 100.0;
        }

        if (concentration <= 15.0) {
            return 85.0;
        }

        if (concentration <= 30.0) {
            return 70.0;
        }

        if (concentration <= 50.0) {
            return 50.0;
        }

        return 20.0;
    }

    /**
     * Identify supplier risk flags.
     */
    private void identifyRiskFlags(
            ScoreResult result,
            PurchaseRequest request,
            Supplier supplier,
            QuotationItem quotation) {

        if (result.getRiskFlags() == null) {
            return;
        }

        // Delivery risk
        if (result.getDeliveryScore() == 0) {

            result.getRiskFlags().add(
                    "DELIVERY_FAILED: Cannot meet required date"
            );

        } else if (result.getDeliveryScore() < 80) {

            result.getRiskFlags().add(
                    "DELIVERY_RISK: Tight delivery timeline"
            );
        }

        // Capacity risk
        if (result.getCapacityScore() == 0) {

            result.getRiskFlags().add(
                    "CAPACITY_FAILED: Cannot fulfill quantity"
            );

        } else if (result.getCapacityScore() < 80) {

            result.getRiskFlags().add(
                    "CAPACITY_RISK: Limited capacity"
            );
        }

        // Financial risk
        if (supplier.getFinancialHealthScore() != null
                && supplier.getFinancialHealthScore() < 60) {

            result.getRiskFlags().add(
                    "FINANCIAL_RISK: Poor financial health"
            );
        }

        // Dependency risk
        if (supplier.getPurchaseConcentration() != null
                && supplier.getPurchaseConcentration() > 50) {

            result.getRiskFlags().add(
                    "DEPENDENCY_RISK: High supplier concentration"
            );
        }
    }

    private double clamp(
            double value,
            double min,
            double max) {

        return Math.max(
                min,
                Math.min(max, value)
        );
    }
}
