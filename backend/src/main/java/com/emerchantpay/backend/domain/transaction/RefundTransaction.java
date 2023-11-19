package com.emerchantpay.backend.domain.transaction;

import jakarta.persistence.Entity;

@Entity
public class RefundTransaction extends AmountTransaction {

	@Override
	public TransactionType getType() {
		return TransactionType.TRANSACTION_REFUND;
	}
}
