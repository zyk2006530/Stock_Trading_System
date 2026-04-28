package com.stocktrading.repository;

import com.stocktrading.entity.Trade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
	List<Trade> findTop20ByStockCodeOrderByCreatedAtDesc(String stockCode);
	List<Trade> findByStockCodeOrderByCreatedAtDesc(String stockCode);
    List<Trade> findByBuyerUserIdOrSellerUserIdOrderByCreatedAtDesc(Long buyerUserId, Long sellerUserId);
    List<Trade> findAllByOrderByCreatedAtDesc();
}
