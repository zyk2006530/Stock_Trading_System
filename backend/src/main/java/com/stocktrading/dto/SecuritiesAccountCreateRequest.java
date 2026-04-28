package com.stocktrading.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SecuritiesAccountCreateRequest {
    @NotNull
    private Long userId;

    private Long fundAccountId;
}
