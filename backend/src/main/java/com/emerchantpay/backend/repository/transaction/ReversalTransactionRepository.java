package com.emerchantpay.backend.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emerchantpay.backend.domain.transaction.ReversalTransaction;

@Repository
public interface ReversalTransactionRepository extends JpaRepository<ReversalTransaction, Long> {
}
