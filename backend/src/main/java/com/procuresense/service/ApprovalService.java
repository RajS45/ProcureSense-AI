package com.procuresense.service;

import com.procuresense.model.Approval;
import com.procuresense.model.ApprovalDecision;
import com.procuresense.model.PurchaseRequest;
import com.procuresense.model.Recommendation;
import com.procuresense.model.RecommendationStatus;
import com.procuresense.model.RequestStatus;
import com.procuresense.model.SapSyncStatus;
import com.procuresense.model.User;
import com.procuresense.repository.ApprovalRepository;
import com.procuresense.repository.RecommendationRepository;
import com.procuresense.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseRequestService purchaseRequestService;

    @Transactional
    public void approveRecommendation(
            Long recommendationId,
            Long approverId,
            String comments) {

        Recommendation recommendation
                = recommendationRepository
                        .findById(recommendationId)
                        .orElseThrow(()
                                -> new RuntimeException(
                                "Recommendation not found"
                        )
                        );

        User approver
                = userRepository
                        .findById(approverId)
                        .orElseThrow(()
                                -> new RuntimeException(
                                "Approver not found"
                        )
                        );

        Approval approval
                = Approval.builder()
                        .recommendation(recommendation)
                        .approver(approver)
                        .decision(
                                ApprovalDecision.APPROVED
                        )
                        .comments(comments)
                        .sapSyncStatus(
                                SapSyncStatus.PENDING
                        )
                        .approvalDate(
                                LocalDateTime.now()
                        )
                        .build();

        approvalRepository.save(approval);

        recommendation.setStatus(
                RecommendationStatus.APPROVED
        );

        recommendationRepository.save(
                recommendation
        );

        PurchaseRequest request
                = recommendation.getPurchaseRequest();

        if (request != null) {

            purchaseRequestService.updateStatus(
                    request.getId(),
                    RequestStatus.APPROVED
            );
        }

        log.info(
                "Recommendation {} approved by user {}",
                recommendationId,
                approverId
        );
    }

    @Transactional
    public void rejectRecommendation(
            Long recommendationId,
            Long approverId,
            String reason) {

        Recommendation recommendation
                = recommendationRepository
                        .findById(recommendationId)
                        .orElseThrow(()
                                -> new RuntimeException(
                                "Recommendation not found"
                        )
                        );

        User approver
                = userRepository
                        .findById(approverId)
                        .orElseThrow(()
                                -> new RuntimeException(
                                "Approver not found"
                        )
                        );

        Approval approval
                = Approval.builder()
                        .recommendation(recommendation)
                        .approver(approver)
                        .decision(
                                ApprovalDecision.REJECTED
                        )
                        .comments(reason)
                        .sapSyncStatus(
                                SapSyncStatus.NOT_APPLICABLE
                        )
                        .approvalDate(
                                LocalDateTime.now()
                        )
                        .build();

        approvalRepository.save(approval);

        recommendation.setStatus(
                RecommendationStatus.REJECTED
        );

        recommendationRepository.save(
                recommendation
        );

        log.info(
                "Recommendation {} rejected by user {}",
                recommendationId,
                approverId
        );
    }
}
