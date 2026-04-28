package com.stocktrading.vo;

import com.stocktrading.entity.Trade;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeResponse {
    private Long id;
    private String tradeNo;
    private String stockCode;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public static TradeResponse from(Trade trade) {
        return new TradeResponse(
            trade.getId(),
            trade.getTradeNo(),
            trade.getStockCode(),
            trade.getPrice(),
            trade.getQuantity(),
            trade.getAmount(),
            trade.getCreatedAt()
        );
    }
}
