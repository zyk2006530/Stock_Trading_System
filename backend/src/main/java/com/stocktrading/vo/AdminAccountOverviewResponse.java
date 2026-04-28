package com.stocktrading.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountOverviewResponse {
    private List<FundAccountResponse> fundAccounts;
    private List<SecuritiesAccountResponse> securitiesAccounts;
    private List<HoldingResponse> holdings;
}
