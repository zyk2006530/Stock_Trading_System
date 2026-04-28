package com.stocktrading.vo;

import com.stocktrading.entity.FundAccount;
import com.stocktrading.entity.enums.FundAccountStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundAccountResponse {
    private Long id;
    private String accountNo;
    private Long userId;
    private BigDecimal balance;
    private BigDecimal frozenBalance;
    private FundAccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FundAccountResponse from(FundAccount account) {
        return new FundAccountResponse(
            account.getId(),
            account.getAccountNo(),
            account.getUserId(),
            account.getBalance(),
            account.getFrozenBalance(),
            account.getStatus(),
            account.getCreatedAt(),
            account.getUpdatedAt()
        );
    }
}
