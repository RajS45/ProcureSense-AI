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
public class QuotationDTO {

    private Long id;
    private Long requestId;
    private Long supplierId;
    private String supplierName;
    private String status;
    private Double extractionConfidence;
    private QuotationItemDTO item;
    private LocalDateTime uploadedAt;
}