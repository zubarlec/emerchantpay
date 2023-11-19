package com.emerchantpay.backend.domain.transaction;

import jakarta.persistence.Entity;

@Entity
public class ReversalTransaction extends Transaction {

	@Override
	public TransactionType getType() {
		return TransactionType.TRANSACTION_REVERSAL;
	}
}
