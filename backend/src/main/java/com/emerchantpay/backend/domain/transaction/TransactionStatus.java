package com.emerchantpay.backend.domain.transaction;

public enum TransactionStatus {
	TRANSACTION_APPROVED(true),
	TRANSACTION_REVERSED(false),
	TRANSACTION_REFUNDED(true),
	TRANSACTION_ERROR(false);

	private final boolean allowReference;

	TransactionStatus(boolean allowReference) {
		this.allowReference = allowReference;
	}

	public boolean isAllowReference() {
		return allowReference;
	}
}
