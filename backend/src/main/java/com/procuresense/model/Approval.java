package com.procuresense.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "approvals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id", nullable = false)
    private Recommendation recommendation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;

    @Enumerated(EnumType.STRING)
    private ApprovalDecision decision;

    private String comments;

    @Enumerated(EnumType.STRING)
    private SapSyncStatus sapSyncStatus = SapSyncStatus.PENDING;

    private String sapTransactionId;
    private String sapErrorMessage;

    private LocalDateTime approvalDate;
    private LocalDateTime sapSyncDate;

    @PrePersist
    protected void onCreate() {
        approvalDate = LocalDateTime.now();
    }
}


