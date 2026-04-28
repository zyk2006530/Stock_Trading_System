package com.stocktrading.controller;

import com.stocktrading.common.ApiResponse;
import com.stocktrading.dto.SecuritiesAccountCreateRequest;
import com.stocktrading.service.StaffSecuritiesAccountService;
import com.stocktrading.vo.SecuritiesAccountResponse;
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
@RequestMapping("/api/staff/securities-accounts")
@RequiredArgsConstructor
public class StaffSecuritiesAccountController {
    private final StaffSecuritiesAccountService staffSecuritiesAccountService;

    @PostMapping
    public ApiResponse<SecuritiesAccountResponse> create(@Valid @RequestBody SecuritiesAccountCreateRequest request) {
        return ApiResponse.success(staffSecuritiesAccountService.createSecuritiesAccount(request));
    }

    @PutMapping("/{id}/lost")
    public ApiResponse<SecuritiesAccountResponse> markLost(@PathVariable Long id) {
        return ApiResponse.success(staffSecuritiesAccountService.markLost(id));
    }

    @PutMapping("/{id}/reopen")
    public ApiResponse<SecuritiesAccountResponse> reopen(@PathVariable Long id) {
        return ApiResponse.success(staffSecuritiesAccountService.reopen(id));
    }

    @PutMapping("/{id}/close")
    public ApiResponse<SecuritiesAccountResponse> close(@PathVariable Long id) {
        return ApiResponse.success(staffSecuritiesAccountService.close(id));
    }

    @GetMapping("/{id}")
    public ApiResponse<SecuritiesAccountResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(staffSecuritiesAccountService.getById(id));
    }
}
