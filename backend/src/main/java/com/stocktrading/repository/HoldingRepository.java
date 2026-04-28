package com.stocktrading.repository;

import com.stocktrading.entity.Holding;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
    Optional<Holding> findBySecuritiesAccountIdAndStockCode(Long securitiesAccountId, String stockCode);
    List<Holding> findBySecuritiesAccountId(Long securitiesAccountId);
}
