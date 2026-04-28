package com.stocktrading.vo;

import com.stocktrading.entity.Holding;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoldingResponse {
    private Long id;
    private String stockCode;
    private String stockName;
    private Integer quantity;
    private Integer frozenQuantity;
    private LocalDateTime updatedAt;

    public static HoldingResponse from(Holding holding, String stockName) {
        return new HoldingResponse(
            holding.getId(),
            holding.getStockCode(),
            stockName,
            holding.getQuantity(),
            holding.getFrozenQuantity(),
            holding.getUpdatedAt()
        );
    }
}
