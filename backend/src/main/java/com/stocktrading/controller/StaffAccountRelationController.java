package com.stocktrading.controller;

import com.stocktrading.common.ApiResponse;
import com.stocktrading.dto.AccountRelationRequest;
import com.stocktrading.service.StaffAccountRelationService;
import com.stocktrading.vo.AccountRelationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff/account-relations")
@RequiredArgsConstructor
public class StaffAccountRelationController {
    private final StaffAccountRelationService staffAccountRelationService;

    @PostMapping
    public ApiResponse<AccountRelationResponse> link(@Valid @RequestBody AccountRelationRequest request) {
        return ApiResponse.success(staffAccountRelationService.link(request));
    }
}
