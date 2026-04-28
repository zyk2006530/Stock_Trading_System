package com.stocktrading.vo;

import com.stocktrading.entity.FundFlow;
import com.stocktrading.entity.enums.FundFlowType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundFlowResponse {
    private Long id;
    private Long fundAccountId;
    private Long userId;
    private FundFlowType type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String remark;
    private LocalDateTime createdAt;

    public static FundFlowResponse from(FundFlow flow) {
        return new FundFlowResponse(
            flow.getId(),
            flow.getFundAccountId(),
            flow.getUserId(),
            flow.getType(),
            flow.getAmount(),
            flow.getBalanceAfter(),
            flow.getRemark(),
            flow.getCreatedAt()
        );
    }
}
