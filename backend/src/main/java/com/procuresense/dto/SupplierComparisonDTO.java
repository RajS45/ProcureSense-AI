package com.procuresense.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierComparisonDTO {

    private Long requestId;
    private List<SupplierScoreDTO> suppliers;
    private String recommendedSupplierName;
    private Long recommendedSupplierId;
}