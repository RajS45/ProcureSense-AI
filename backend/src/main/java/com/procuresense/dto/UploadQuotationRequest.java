package com.procuresense.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadQuotationRequest {

    private Long requestId;
    private Long supplierId;
    private String fileName;
}