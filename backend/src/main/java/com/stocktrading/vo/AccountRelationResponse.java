package com.stocktrading.vo;

import com.stocktrading.entity.AccountRelation;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRelationResponse {
    private Long id;
    private Long fundAccountId;
    private Long securitiesAccountId;
    private LocalDateTime createdAt;

    public static AccountRelationResponse from(AccountRelation relation) {
        return new AccountRelationResponse(
            relation.getId(),
            relation.getFundAccountId(),
            relation.getSecuritiesAccountId(),
            relation.getCreatedAt()
        );
    }
}
