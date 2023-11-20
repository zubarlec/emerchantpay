package com.emerchantpay.backend.repository.transaction;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.emerchantpay.backend.domain.transaction.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	void deleteByTimestampLessThan(long timestamp);

	@Query("SELECT DISTINCT t.merchant.id FROM Transaction t WHERE t.timestamp < ?1")
	Set<Long> findMerchantIdsByTimestampLessThan(long timestamp);
}
