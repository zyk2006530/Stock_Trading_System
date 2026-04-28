package com.stocktrading.repository;

import com.stocktrading.entity.Stock;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByCode(String code);
    boolean existsByCode(String code);
    List<Stock> findByCodeContainingOrNameContainingIgnoreCase(String code, String name);
    List<Stock> findByCodeIn(List<String> codes);
}
