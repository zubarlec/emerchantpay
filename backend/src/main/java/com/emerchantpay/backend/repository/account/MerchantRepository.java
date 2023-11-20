package com.emerchantpay.backend.repository.account;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.emerchantpay.backend.domain.account.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
	@Modifying
	@Query("UPDATE Merchant merchant SET merchant.totalTransactionSum = (SELECT SUM(ct.amount) FROM ChargeTransaction ct WHERE ct.merchant = merchant AND ct.status = 'TRANSACTION_APPROVED') WHERE merchant.id IN (?1)")
	void updateTotalSum(Set<Long> ids);
}
