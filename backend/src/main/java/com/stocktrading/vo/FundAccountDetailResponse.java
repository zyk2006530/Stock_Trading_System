package com.stocktrading.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundAccountDetailResponse {
    private FundAccountResponse account;
    private List<FundFlowResponse> flows;
}
