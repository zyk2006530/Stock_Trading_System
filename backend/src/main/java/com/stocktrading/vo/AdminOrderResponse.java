package com.stocktrading.vo;

import com.stocktrading.entity.StockOrder;
import com.stocktrading.entity.enums.OrderDirection;
import com.stocktrading.entity.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderResponse {
    private Long id;
    private String orderNo;
    private Long userId;
    private String stockCode;
    private OrderDirection direction;
    private BigDecimal price;
    private Integer quantity;
    private Integer filledQuantity;
    private Integer remainingQuantity;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AdminOrderResponse from(StockOrder order) {
        return new AdminOrderResponse(
            order.getId(),
            order.getOrderNo(),
            order.getUserId(),
            order.getStockCode(),
            order.getDirection(),
            order.getPrice(),
            order.getQuantity(),
            order.getFilledQuantity(),
            order.getRemainingQuantity(),
            order.getStatus(),
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }
}
