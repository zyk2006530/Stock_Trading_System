package com.stocktrading.controller;

import com.stocktrading.common.ApiResponse;
import com.stocktrading.security.CustomUserDetails;
import com.stocktrading.service.ClientHistoryService;
import com.stocktrading.vo.StockOrderResponse;
import com.stocktrading.vo.TradeResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientHistoryController {
    private final ClientHistoryService clientHistoryService;

    @GetMapping("/orders")
    public ApiResponse<List<StockOrderResponse>> orders(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ApiResponse.success(clientHistoryService.listOrders(userDetails.getUser().getId()));
    }

    @GetMapping("/trades")
    public ApiResponse<List<TradeResponse>> trades(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ApiResponse.success(clientHistoryService.listTrades(userDetails.getUser().getId()));
    }
}
