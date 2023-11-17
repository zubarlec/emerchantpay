package com.emerchantpay.backend.domain.builder.transaction;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.transaction.RefundTransaction;

@Configurable
public class RefundTransactionBuilder extends AmountTransactionBuilder<RefundTransaction> {
	public RefundTransactionBuilder(BigDecimal amount, String customerEmail, Merchant merchant) {
		super(amount, customerEmail, merchant);
	}

	@Override
	protected RefundTransaction createNew() {
		return new RefundTransaction();
	}

	@Override
	protected JpaRepository<RefundTransaction, Long> getRepository() {
		return repo.refundTransaction;
	}
}
