package com.procuresense.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDTO {

    private Long id;
    private String decision;
    private String comments;
    private String approverName;
    private LocalDateTime approvalDate;
    private String sapSyncStatus;
}