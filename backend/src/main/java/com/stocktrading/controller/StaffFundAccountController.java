package com.stocktrading.controller;

import com.stocktrading.common.ApiResponse;
import com.stocktrading.dto.FundAccountAmountRequest;
import com.stocktrading.dto.FundAccountCreateRequest;
import com.stocktrading.dto.FundAccountPasswordUpdateRequest;
import com.stocktrading.service.StaffFundAccountService;
import com.stocktrading.vo.FundAccountDetailResponse;
import com.stocktrading.vo.FundAccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff/fund-accounts")
@RequiredArgsConstructor
public class StaffFundAccountController {
    private final StaffFundAccountService staffFundAccountService;

    @PostMapping
    public ApiResponse<FundAccountResponse> create(@Valid @RequestBody FundAccountCreateRequest request) {
        return ApiResponse.success(staffFundAccountService.createFundAccount(request));
    }

    @PutMapping("/{id}/approve")
    public ApiResponse<FundAccountResponse> approve(@PathVariable Long id) {
        return ApiResponse.success(staffFundAccountService.approveFundAccount(id));
    }

    @PutMapping("/{id}/password")
    public ApiResponse<FundAccountResponse> updatePassword(@PathVariable Long id,
        @Valid @RequestBody FundAccountPasswordUpdateRequest request) {
        return ApiResponse.success(staffFundAccountService.updatePassword(id, request));
    }

    @PostMapping("/{id}/deposit")
    public ApiResponse<FundAccountResponse> deposit(@PathVariable Long id,
        @Valid @RequestBody FundAccountAmountRequest request) {
        return ApiResponse.success(staffFundAccountService.deposit(id, request));
    }

    @PostMapping("/{id}/withdraw")
    public ApiResponse<FundAccountResponse> withdraw(@PathVariable Long id,
        @Valid @RequestBody FundAccountAmountRequest request) {
        return ApiResponse.success(staffFundAccountService.withdraw(id, request));
    }

    @PutMapping("/{id}/lost")
    public ApiResponse<FundAccountResponse> markLost(@PathVariable Long id) {
        return ApiResponse.success(staffFundAccountService.markLost(id));
    }

    @PutMapping("/{id}/reopen")
    public ApiResponse<FundAccountResponse> reopen(@PathVariable Long id) {
        return ApiResponse.success(staffFundAccountService.reopen(id));
    }

    @PutMapping("/{id}/close")
    public ApiResponse<FundAccountResponse> close(@PathVariable Long id) {
        return ApiResponse.success(staffFundAccountService.close(id));
    }

    @GetMapping("/{id}")
    public ApiResponse<FundAccountDetailResponse> getDetail(@PathVariable Long id) {
        return ApiResponse.success(staffFundAccountService.getDetail(id));
    }
}
