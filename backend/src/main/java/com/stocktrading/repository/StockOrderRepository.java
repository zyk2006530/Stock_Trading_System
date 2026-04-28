package com.stocktrading.repository;

import com.stocktrading.entity.StockOrder;
import com.stocktrading.entity.enums.OrderDirection;
import com.stocktrading.entity.enums.OrderStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockOrderRepository extends JpaRepository<StockOrder, Long> {
	List<StockOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
	List<StockOrder> findByStockCodeOrderByCreatedAtDesc(String stockCode);
	List<StockOrder> findAllByOrderByCreatedAtDesc();
	List<StockOrder> findByStockCodeAndDirectionAndStatusInOrderByPriceDescCreatedAtAsc(
		String stockCode,
		OrderDirection direction,
		List<OrderStatus> statuses
	);
	List<StockOrder> findByStockCodeAndDirectionAndStatusInOrderByPriceAscCreatedAtAsc(
		String stockCode,
		OrderDirection direction,
		List<OrderStatus> statuses
	);
}
