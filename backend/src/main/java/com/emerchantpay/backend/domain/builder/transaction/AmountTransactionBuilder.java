package com.emerchantpay.backend.domain.builder.transaction;

import java.math.BigDecimal;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.transaction.AmountTransaction;

public abstract class AmountTransactionBuilder<T extends AmountTransaction> extends BaseTransactionBuilder<T> {

	protected AmountTransactionBuilder(BigDecimal amount, String customerEmail, Merchant merchant) {
		super(customerEmail, merchant);
		result.setAmount(amount);
	}
}
