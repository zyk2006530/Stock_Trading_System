package com.stocktrading.controller;

import com.stocktrading.common.ApiResponse;
import com.stocktrading.service.StockService;
import com.stocktrading.vo.StockResponse;
import com.stocktrading.vo.StockStatsResponse;
import com.stocktrading.vo.TradeResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping
    public ApiResponse<List<StockResponse>> list() {
        return ApiResponse.success(stockService.listAll());
    }

    @GetMapping("/{code}")
    public ApiResponse<StockResponse> getByCode(@PathVariable String code) {
        return ApiResponse.success(stockService.getByCode(code));
    }

    @GetMapping("/search")
    public ApiResponse<List<StockResponse>> search(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(stockService.search(keyword));
    }

    @GetMapping("/{code}/trades")
    public ApiResponse<List<TradeResponse>> trades(@PathVariable String code) {
        return ApiResponse.success(stockService.recentTrades(code));
    }

    @GetMapping("/{code}/stats")
    public ApiResponse<StockStatsResponse> stats(@PathVariable String code) {
        return ApiResponse.success(stockService.stats(code));
    }
}
