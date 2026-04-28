package com.stocktrading.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class FundAccountCreateRequest {
    @NotNull
    private Long userId;

    @NotBlank
    private String password;

    @DecimalMin("0.00")
    private BigDecimal initialBalance;
}
