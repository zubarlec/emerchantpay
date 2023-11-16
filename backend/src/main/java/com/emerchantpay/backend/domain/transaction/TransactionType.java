package com.emerchantpay.backend.domain.transaction;

import java.util.Set;

public enum TransactionType {
	TRANSACTION_AUTHORIZE(null),
	TRANSACTION_CHARGE(Set.of(TRANSACTION_AUTHORIZE)),
	TRANSACTION_REFUND(Set.of(TRANSACTION_CHARGE)),
	TRANSACTION_REVERSAL(Set.of(TRANSACTION_AUTHORIZE));

	private final Set<TransactionType> allowedReferenceType;

	TransactionType(Set<TransactionType> allowedReferenceType) {
		this.allowedReferenceType = allowedReferenceType;
	}

	public boolean canReference(TransactionType referenceType) {
		return allowedReferenceType != null && referenceType != null && allowedReferenceType.contains(referenceType) || (allowedReferenceType == null && referenceType == null);
	}

	public static TransactionType getType(Transaction transaction) {
		if (transaction instanceof AuthorizeTransaction) {
			return TRANSACTION_AUTHORIZE;
		} else if (transaction instanceof ChargeTransaction) {
			return TRANSACTION_CHARGE;
		} else if (transaction instanceof RefundTransaction) {
			return TRANSACTION_REFUND;
		} else if (transaction instanceof ReversalTransaction) {
			return TRANSACTION_REVERSAL;
		}
		return null;
	}
}
