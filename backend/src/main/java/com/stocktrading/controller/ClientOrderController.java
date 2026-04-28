package com.stocktrading.controller;

import com.stocktrading.common.ApiResponse;
import com.stocktrading.dto.OrderCreateRequest;
import com.stocktrading.security.CustomUserDetails;
import com.stocktrading.service.ClientOrderService;
import com.stocktrading.vo.StockOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/orders")
@RequiredArgsConstructor
public class ClientOrderController {
    private final ClientOrderService clientOrderService;

    @PostMapping("/buy")
    public ApiResponse<StockOrderResponse> buy(Authentication authentication,
        @Valid @RequestBody OrderCreateRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ApiResponse.success(clientOrderService.buy(userDetails.getUser().getId(), request));
    }

    @PostMapping("/sell")
    public ApiResponse<StockOrderResponse> sell(Authentication authentication,
        @Valid @RequestBody OrderCreateRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ApiResponse.success(clientOrderService.sell(userDetails.getUser().getId(), request));
    }
}
