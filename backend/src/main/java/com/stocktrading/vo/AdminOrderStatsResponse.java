package com.stocktrading.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderStatsResponse {
    private String stockCode;
    private long buyOrders;
    private long sellOrders;
    private long buyQuantity;
    private long sellQuantity;
}
