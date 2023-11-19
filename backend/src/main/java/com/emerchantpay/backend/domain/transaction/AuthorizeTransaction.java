package com.emerchantpay.backend.domain.transaction;

import jakarta.persistence.Entity;

@Entity
public class AuthorizeTransaction extends AmountTransaction {

	@Override
	public TransactionType getType() {
		return TransactionType.TRANSACTION_AUTHORIZE;
	}
}
