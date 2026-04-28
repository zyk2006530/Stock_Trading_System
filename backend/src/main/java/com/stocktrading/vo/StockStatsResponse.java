package com.stocktrading.vo;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockStatsResponse {
    private String stockCode;
    private BigDecimal latestPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private Long totalVolume;
    private BigDecimal totalAmount;
}
