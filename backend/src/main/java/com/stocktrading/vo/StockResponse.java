package com.stocktrading.vo;

import com.stocktrading.entity.Stock;
import com.stocktrading.entity.enums.StockStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {
    private Long id;
    private String code;
    private String name;
    private BigDecimal initialPrice;
    private BigDecimal latestPrice;
    private StockStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StockResponse from(Stock stock) {
        return new StockResponse(
            stock.getId(),
            stock.getCode(),
            stock.getName(),
            stock.getInitialPrice(),
            stock.getLatestPrice(),
            stock.getStatus(),
            stock.getCreatedAt(),
            stock.getUpdatedAt()
        );
    }
}
