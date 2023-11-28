package com.emerchantpay.backend.service.transaction.impl;

import org.springframework.stereotype.Service;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.builder.transaction.*;
import com.emerchantpay.backend.domain.transaction.Transaction;
import com.emerchantpay.backend.domain.transaction.TransactionStatus;
import com.emerchantpay.backend.dto.transaction.TransactionDTO;
import com.emerchantpay.backend.service.transaction.TransactionFactory;

@Service
public class TransactionFactoryImpl implements TransactionFactory {

	@Override
	public Transaction createTransaction(TransactionDTO transaction, Transaction reference, Merchant merchant) {
		BaseTransactionBuilder<?> builder = switch (transaction.getType()) {
			case TRANSACTION_AUTHORIZE -> new AuthorizeTransactionBuilder(transaction.getAmount(), transaction.getCustomerEmail(), merchant);
			case TRANSACTION_CHARGE -> new ChargeTransactionBuilder(transaction.getAmount(), transaction.getCustomerEmail(), merchant);
			case TRANSACTION_REFUND -> new RefundTransactionBuilder(transaction.getAmount(), transaction.getCustomerEmail(), merchant);
			case TRANSACTION_REVERSAL -> new ReversalTransactionBuilder(transaction.getCustomerEmail(), merchant);
		};

		builder.withCustomerPhone(transaction.getCustomerPhone()).withReferenceTransaction(reference);
		if (reference != null && !reference.getStatus().isAllowReference()) {
			builder.withStatus(TransactionStatus.TRANSACTION_ERROR);
		}

		return builder.build();
	}
}
