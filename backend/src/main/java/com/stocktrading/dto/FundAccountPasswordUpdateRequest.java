package com.stocktrading.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FundAccountPasswordUpdateRequest {
    @NotBlank
    private String newPassword;
}
