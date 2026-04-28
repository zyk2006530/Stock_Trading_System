package com.stocktrading.repository;

import com.stocktrading.entity.SecuritiesAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecuritiesAccountRepository extends JpaRepository<SecuritiesAccount, Long> {
    Optional<SecuritiesAccount> findByUserId(Long userId);
    boolean existsByAccountNo(String accountNo);
}
