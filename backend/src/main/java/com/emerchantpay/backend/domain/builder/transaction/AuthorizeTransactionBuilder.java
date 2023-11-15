package com.emerchantpay.backend.domain.builder.transaction;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.emerchantpay.backend.domain.transaction.AuthorizeTransaction;

@Configurable
public class AuthorizeTransactionBuilder extends AmountTransactionBuilder<AuthorizeTransaction> {

	public AuthorizeTransactionBuilder(BigDecimal amount, String customerEmail) {
		super(amount, customerEmail);
	}

	@Override
	protected JpaRepository<AuthorizeTransaction, Long> getRepository() {
		return repo.authorizeTransaction;
	}

	@Override
	protected AuthorizeTransaction createNew() {
		return new AuthorizeTransaction();
	}
}
