package com.procuresense.service;

import com.procuresense.dto.SupplierComparisonDTO;
import com.procuresense.dto.SupplierScoreDTO;
import com.procuresense.model.*;
import com.procuresense.repository.*;
import com.procuresense.scoring.ScoreResult;
import com.procuresense.scoring.ScoringConfiguration;
import com.procuresense.scoring.ScoringEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class ScoringService {

    @Autowired
    private SupplierScoreRepository supplierScoreRepository;

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private QuotationItemRepository quotationItemRepository;

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ScoringEngine scoringEngine;

    @Transactional
    public SupplierComparisonDTO scoreAllSuppliersForRequest(Long requestId) {

        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));

        List<Quotation> quotations =
                quotationRepository.findByPurchaseRequest(request);

        ScoringConfiguration config = new ScoringConfiguration();

        List<SupplierScoreDTO> scores = new ArrayList<>();

        String recommendedSupplier = null;
        Long recommendedSupplierId = null;
        double highestScore = -1;

        for (Quotation quotation : quotations) {

            if (quotation.getSupplier() == null) {
                continue;
            }

            Optional<QuotationItem> itemOpt =
                    quotationItemRepository.findByQuotation(quotation);

            if (itemOpt.isEmpty()) {
                continue;
            }

            QuotationItem item = itemOpt.get();
            Supplier supplier = quotation.getSupplier();

            ScoreResult scoreResult =
                    scoringEngine.calculateScore(
                            request,
                            supplier,
                            item,
                            config
                    );

            SupplierScore scoreEntity = new SupplierScore();

            scoreEntity.setQuotation(quotation);
            scoreEntity.setPurchaseRequest(request);
            scoreEntity.setSupplier(supplier);

            scoreEntity.setCostScore(scoreResult.getCostScore());
            scoreEntity.setQualityScore(scoreResult.getQualityScore());
            scoreEntity.setDeliveryScore(scoreResult.getDeliveryScore());
            scoreEntity.setReliabilityScore(scoreResult.getReliabilityScore());
            scoreEntity.setCapacityScore(scoreResult.getCapacityScore());
            scoreEntity.setPaymentScore(scoreResult.getPaymentScore());
            scoreEntity.setRiskScore(scoreResult.getRiskScore());
            scoreEntity.setDependencyScore(scoreResult.getDependencyScore());

            scoreEntity.setCostContribution(scoreResult.getCostContribution());
            scoreEntity.setQualityContribution(scoreResult.getQualityContribution());
            scoreEntity.setDeliveryContribution(scoreResult.getDeliveryContribution());
            scoreEntity.setReliabilityContribution(scoreResult.getReliabilityContribution());
            scoreEntity.setCapacityContribution(scoreResult.getCapacityContribution());
            scoreEntity.setPaymentContribution(scoreResult.getPaymentContribution());
            scoreEntity.setRiskContribution(scoreResult.getRiskContribution());
            scoreEntity.setDependencyContribution(scoreResult.getDependencyContribution());

            scoreEntity.setFinalWeightedScore(
                    scoreResult.getFinalWeightedScore()
            );

            scoreEntity.setRiskFlags(
                    String.join(", ", scoreResult.getRiskFlags())
            );

            supplierScoreRepository.save(scoreEntity);

            if (scoreResult.getFinalWeightedScore() > highestScore) {
                highestScore = scoreResult.getFinalWeightedScore();
                recommendedSupplier = supplier.getName();
                recommendedSupplierId = supplier.getId();
            }

            scores.add(toScoreDTO(scoreEntity));
        }

        scores.sort(
                (a, b) -> b.getFinalWeightedScore()
                        .compareTo(a.getFinalWeightedScore())
        );

        for (int i = 0; i < scores.size(); i++) {
            scores.get(i).setRanking(i + 1);
        }

        log.info(
                "Scored {} suppliers for request: {}",
                scores.size(),
                requestId
        );

        return new SupplierComparisonDTO(
                requestId,
                scores,
                recommendedSupplier,
                recommendedSupplierId
        );
    }

    private SupplierScoreDTO toScoreDTO(SupplierScore score) {

        List<String> riskFlags = new ArrayList<>();

        if (score.getRiskFlags() != null
                && !score.getRiskFlags().isEmpty()) {

            riskFlags = Arrays.asList(
                    score.getRiskFlags().split(", ")
            );
        }

        return new SupplierScoreDTO(
                score.getId(),
                score.getSupplier().getId(),
                score.getSupplier().getName(),
                score.getCostScore(),
                score.getQualityScore(),
                score.getDeliveryScore(),
                score.getReliabilityScore(),
                score.getCapacityScore(),
                score.getPaymentScore(),
                score.getRiskScore(),
                score.getDependencyScore(),
                score.getFinalWeightedScore(),
                score.getRanking(),
                riskFlags
        );
    }
}