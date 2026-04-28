package com.stocktrading.repository;

import com.stocktrading.entity.FundFlow;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundFlowRepository extends JpaRepository<FundFlow, Long> {
	List<FundFlow> findByFundAccountIdOrderByCreatedAtDesc(Long fundAccountId);
}
