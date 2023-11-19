package com.emerchantpay.backend.domain.transaction;

import jakarta.persistence.Entity;

@Entity
public class ChargeTransaction extends AmountTransaction {

	@Override
	public TransactionType getType() {
		return TransactionType.TRANSACTION_CHARGE;
	}
}
