package com.stocktrading.repository;

import com.stocktrading.entity.FundAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundAccountRepository extends JpaRepository<FundAccount, Long> {
    Optional<FundAccount> findByUserId(Long userId);
    boolean existsByAccountNo(String accountNo);
}
