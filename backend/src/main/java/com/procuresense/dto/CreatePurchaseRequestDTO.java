package com.procuresense.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePurchaseRequestDTO {

    private Long productId;
    private Long quantity;
    private LocalDate requiredByDate;
    private BigDecimal budget;
    private Integer currentInventory;
    private String priority;
    private String notes;
}