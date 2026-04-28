package com.stocktrading.vo;

import com.stocktrading.entity.SecuritiesAccount;
import com.stocktrading.entity.enums.SecuritiesAccountStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecuritiesAccountResponse {
    private Long id;
    private String accountNo;
    private Long userId;
    private SecuritiesAccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SecuritiesAccountResponse from(SecuritiesAccount account) {
        return new SecuritiesAccountResponse(
            account.getId(),
            account.getAccountNo(),
            account.getUserId(),
            account.getStatus(),
            account.getCreatedAt(),
            account.getUpdatedAt()
        );
    }
}
