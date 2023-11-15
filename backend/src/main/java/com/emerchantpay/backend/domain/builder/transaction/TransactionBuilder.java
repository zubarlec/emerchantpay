package com.emerchantpay.backend.domain.builder.transaction;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.emerchantpay.backend.domain.builder.BaseEntityBuilder;
import com.emerchantpay.backend.domain.transaction.Transaction;
import com.emerchantpay.backend.domain.transaction.TransactionStatus;

@Configurable
public class TransactionBuilder extends BaseEntityBuilder<Transaction> {
	@Override
	protected JpaRepository<Transaction, Long> getRepository() {
		return repo.transaction;
	}

	public TransactionBuilder(Transaction other) {
		result = other;
	}

	public TransactionBuilder withStatus(TransactionStatus status) {
		result.setStatus(status);
		return this;
	}
}
