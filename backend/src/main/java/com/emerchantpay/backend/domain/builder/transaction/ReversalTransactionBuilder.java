package com.emerchantpay.backend.domain.builder.transaction;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.transaction.ReversalTransaction;

@Configurable
public class ReversalTransactionBuilder extends BaseTransactionBuilder<ReversalTransaction> {
	public ReversalTransactionBuilder(String customerEmail, Merchant merchant) {
		super(customerEmail, merchant);
	}

	@Override
	protected JpaRepository<ReversalTransaction, Long> getRepository() {
		return repo.reversalTransaction;
	}

	@Override
	protected ReversalTransaction createNew() {
		return new ReversalTransaction();
	}
}
