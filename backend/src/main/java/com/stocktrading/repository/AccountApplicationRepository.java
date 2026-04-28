package com.stocktrading.repository;

import com.stocktrading.entity.AccountApplication;
import com.stocktrading.entity.enums.AccountApplicationStatus;
import com.stocktrading.entity.enums.AccountApplicationType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountApplicationRepository extends JpaRepository<AccountApplication, Long> {
	Optional<AccountApplication> findTopByUserIdAndTypeAndStatusOrderByCreatedAtDesc(
		Long userId,
		AccountApplicationType type,
		AccountApplicationStatus status
	);
}
