package com.emerchantpay.backend.domain.builder.transaction;

import java.util.UUID;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.builder.BaseEntityBuilder;
import com.emerchantpay.backend.domain.transaction.Transaction;
import com.emerchantpay.backend.domain.transaction.TransactionStatus;

public abstract class BaseTransactionBuilder<T extends Transaction> extends BaseEntityBuilder<T> {

	protected abstract T createNew();

	protected BaseTransactionBuilder(String customerEmail) {
		result = createNew();
		result.setUuid(UUID.randomUUID().toString());
		result.setTimestamp(System.currentTimeMillis());
		result.setStatus(TransactionStatus.TRANSACTION_APPROVED);
		result.setCustomerEmail(customerEmail);
	}

	public BaseTransactionBuilder<T> withCustomerPhone(String customerPhone) {
		result.setCustomerPhone(customerPhone);
		return this;
	}
	public BaseTransactionBuilder<T> withStatus(TransactionStatus status) {
		result.setStatus(status);
		return this;
	}
	public BaseTransactionBuilder<T> withReferenceTransaction(Transaction referenceTransaction) {
		result.setReferenceTransaction(referenceTransaction);
		return this;
	}
	public BaseTransactionBuilder<T> withMerchant(Merchant merchant) {
		result.setMerchant(merchant);
		return this;
	}

	/**
	 * Use only for unit tests
	 */
	public BaseTransactionBuilder<T> withTimestamp(long timestamp) {
		result.setTimestamp(timestamp);
		return this;
	}
}
