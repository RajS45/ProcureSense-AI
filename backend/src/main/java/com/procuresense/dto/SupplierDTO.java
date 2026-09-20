package com.procuresense.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {

    private Long id;
    private String supplierId;
    private String name;
    private String location;
    private String status;
    private Double onTimeDeliveryRate;
    private Double qualityScore;
}