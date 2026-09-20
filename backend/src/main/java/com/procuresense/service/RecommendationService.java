package com.procuresense.service;

import com.procuresense.dto.RecommendationDTO;
import com.procuresense.model.PurchaseRequest;
import com.procuresense.model.Recommendation;
import com.procuresense.model.RecommendationStatus;
import com.procuresense.model.SupplierScore;
import com.procuresense.model.User;
import com.procuresense.repository.PurchaseRequestRepository;
import com.procuresense.repository.RecommendationRepository;
import com.procuresense.repository.SupplierScoreRepository;
import com.procuresense.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private SupplierScoreRepository supplierScoreRepository;

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public RecommendationDTO createRecommendation(Long requestId, Long userId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));

        List<SupplierScore> scores =
                supplierScoreRepository.findByPurchaseRequest(request);

        if (scores.isEmpty()) {
            throw new RuntimeException("No suppliers scored for this request");
        }

        SupplierScore topScore = scores.stream()
                .max(Comparator.comparingDouble(
                        SupplierScore::getFinalWeightedScore))
                .orElseThrow();

        SupplierScore alternativeScore = scores.stream()
                .filter(s -> !s.getSupplier().getId()
                        .equals(topScore.getSupplier().getId()))
                .max(Comparator.comparingDouble(
                        SupplierScore::getFinalWeightedScore))
                .orElse(null);

        User user = userRepository.findById(userId).orElse(null);

        String reasoning = generateRecommendationExplanation(topScore);

        Recommendation recommendation = Recommendation.builder()
                .purchaseRequest(request)
                .preferredSupplier(topScore.getSupplier())
                .preferredScore(topScore.getFinalWeightedScore())
                .alternativeSupplier(
                        alternativeScore != null
                                ? alternativeScore.getSupplier()
                                : null)
                .alternativeScore(
                        alternativeScore != null
                                ? alternativeScore.getFinalWeightedScore()
                                : null)
                .reasoning(reasoning)
                .status(RecommendationStatus.RECOMMENDED)
                .createdBy(user)
                .build();

        Recommendation saved =
                recommendationRepository.save(recommendation);

        log.info(
                "Created recommendation for request: {} - Preferred: {}",
                requestId,
                topScore.getSupplier().getName()
        );

        return toDTO(saved);
    }

    public RecommendationDTO getRecommendation(Long requestId) {
        Recommendation recommendation =
                recommendationRepository.findByPurchaseRequest(
                        purchaseRequestRepository.findById(requestId)
                                .orElseThrow())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recommendation not found"));

        return toDTO(recommendation);
    }

    private String generateRecommendationExplanation(
            SupplierScore score) {

        return String.format(
                "Supplier %s is recommended with a score of %.1f due to: " +
                "Cost (%.0f), Quality (%.0f), Delivery (%.0f), " +
                "Reliability (%.0f). This supplier best meets the " +
                "procurement requirements given the current business context.",
                score.getSupplier().getName(),
                score.getFinalWeightedScore(),
                score.getCostScore(),
                score.getQualityScore(),
                score.getDeliveryScore(),
                score.getReliabilityScore()
        );
    }

    private RecommendationDTO toDTO(Recommendation rec) {
        return new RecommendationDTO(
                rec.getId(),
                rec.getPurchaseRequest().getId(),
                rec.getPreferredSupplier().getId(),
                rec.getPreferredSupplier().getName(),
                rec.getPreferredScore(),
                rec.getReasoning(),
                rec.getStatus().name()
        );
    }
}
