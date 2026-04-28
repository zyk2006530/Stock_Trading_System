package com.stocktrading.repository;

import com.stocktrading.entity.AccountRelation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRelationRepository extends JpaRepository<AccountRelation, Long> {
    Optional<AccountRelation> findByFundAccountIdAndSecuritiesAccountId(Long fundAccountId, Long securitiesAccountId);
}
