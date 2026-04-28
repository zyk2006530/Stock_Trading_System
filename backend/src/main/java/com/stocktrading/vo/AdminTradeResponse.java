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
public class AdminTradeResponse {
    private Long id;
    private String tradeNo;
    private String stockCode;
    private Long buyOrderId;
    private Long sellOrderId;
    private Long buyerUserId;
    private Long sellerUserId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public static AdminTradeResponse from(Trade trade) {
        return new AdminTradeResponse(
            trade.getId(),
            trade.getTradeNo(),
            trade.getStockCode(),
            trade.getBuyOrderId(),
            trade.getSellOrderId(),
            trade.getBuyerUserId(),
            trade.getSellerUserId(),
            trade.getPrice(),
            trade.getQuantity(),
            trade.getAmount(),
            trade.getCreatedAt()
        );
    }
}
