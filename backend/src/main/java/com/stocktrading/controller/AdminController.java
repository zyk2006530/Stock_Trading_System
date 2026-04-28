package com.stocktrading.controller;

import com.stocktrading.common.ApiResponse;
import com.stocktrading.service.AdminService;
import com.stocktrading.vo.AdminAccountOverviewResponse;
import com.stocktrading.vo.AdminOrderResponse;
import com.stocktrading.vo.AdminOrderStatsResponse;
import com.stocktrading.vo.AdminTradeResponse;
import com.stocktrading.vo.UserResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/orders")
    public ApiResponse<List<AdminOrderResponse>> orders(@RequestParam(required = false) String stockCode) {
        return ApiResponse.success(adminService.listOrders(stockCode));
    }

    @GetMapping("/trades")
    public ApiResponse<List<AdminTradeResponse>> trades() {
        return ApiResponse.success(adminService.listTrades());
    }

    @GetMapping("/stats/orders")
    public ApiResponse<List<AdminOrderStatsResponse>> orderStats() {
        return ApiResponse.success(adminService.orderStats());
    }

    @GetMapping("/users")
    public ApiResponse<List<UserResponse>> users() {
        return ApiResponse.success(adminService.listUsers());
    }

    @GetMapping("/accounts")
    public ApiResponse<AdminAccountOverviewResponse> accounts() {
        return ApiResponse.success(adminService.listAccounts());
    }
}
