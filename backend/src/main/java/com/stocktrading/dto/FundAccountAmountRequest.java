package com.stocktrading.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class FundAccountAmountRequest {
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    private String remark;
}
