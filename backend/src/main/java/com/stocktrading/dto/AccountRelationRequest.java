package com.stocktrading.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountRelationRequest {
    @NotNull
    private Long fundAccountId;

    @NotNull
    private Long securitiesAccountId;
}
