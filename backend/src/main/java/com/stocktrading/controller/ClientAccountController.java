package com.stocktrading.controller;

import com.stocktrading.common.ApiResponse;
import com.stocktrading.security.CustomUserDetails;
import com.stocktrading.service.ClientAccountService;
import com.stocktrading.vo.FundAccountDetailResponse;
import com.stocktrading.vo.HoldingResponse;
import com.stocktrading.vo.SecuritiesAccountResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientAccountController {
    private final ClientAccountService clientAccountService;

    @GetMapping("/fund-account")
    public ApiResponse<FundAccountDetailResponse> fundAccount(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ApiResponse.success(clientAccountService.getFundAccount(userDetails.getUser().getId()));
    }

    @GetMapping("/securities-account")
    public ApiResponse<SecuritiesAccountResponse> securitiesAccount(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ApiResponse.success(clientAccountService.getSecuritiesAccount(userDetails.getUser().getId()));
    }

    @GetMapping("/holdings")
    public ApiResponse<List<HoldingResponse>> holdings(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ApiResponse.success(clientAccountService.getHoldings(userDetails.getUser().getId()));
    }
}
