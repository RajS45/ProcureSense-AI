package com.procuresense.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestDTO {

    private Long id;
    private String requestCode;
    private Long productId;
    private String productName;
    private Long quantity;
    private LocalDate requiredByDate;
    private BigDecimal budget;
    private Integer inventoryDays;
    private String priority;

    @SuppressWarnings("unused")
    private String status;

    private LocalDateTime createdAt;
}
