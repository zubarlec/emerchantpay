package com.emerchantpay.backend.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emerchantpay.backend.domain.transaction.RefundTransaction;

@Repository
public interface RefundTransactionRepository extends JpaRepository<RefundTransaction, Long> {
}
