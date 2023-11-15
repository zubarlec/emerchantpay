package com.emerchantpay.backend.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emerchantpay.backend.domain.transaction.ChargeTransaction;

@Repository
public interface ChargeTransactionRepository extends JpaRepository<ChargeTransaction, Long> {
}
