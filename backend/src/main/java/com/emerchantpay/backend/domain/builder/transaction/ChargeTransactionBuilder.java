package com.emerchantpay.backend.domain.builder.transaction;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.transaction.ChargeTransaction;

@Configurable
public class ChargeTransactionBuilder extends AmountTransactionBuilder<ChargeTransaction> {

	public ChargeTransactionBuilder(BigDecimal amount, String customerEmail, Merchant merchant) {
		super(amount, customerEmail, merchant);
	}

	@Override
	protected JpaRepository<ChargeTransaction, Long> getRepository() {
		return repo.chargeTransaction;
	}

	@Override
	protected ChargeTransaction createNew() {
		return new ChargeTransaction();
	}
}
