package com.emerchantpay.backend.service.transaction;

import org.springframework.stereotype.Service;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.builder.transaction.*;
import com.emerchantpay.backend.domain.transaction.Transaction;
import com.emerchantpay.backend.domain.transaction.TransactionStatus;
import com.emerchantpay.backend.dto.transaction.TransactionDTO;

@Service
public class TransactionFactory {

	public Transaction createTransaction(TransactionDTO transaction, Transaction reference, Merchant merchant) {
		BaseTransactionBuilder<?> builder = switch (transaction.getType()) {
			case TRANSACTION_AUTHORIZE -> new AuthorizeTransactionBuilder(transaction.getAmount(), transaction.getCustomerEmail());
			case TRANSACTION_CHARGE -> new ChargeTransactionBuilder(transaction.getAmount(), transaction.getCustomerEmail());
			case TRANSACTION_REFUND -> new RefundTransactionBuilder(transaction.getAmount(), transaction.getCustomerEmail());
			case TRANSACTION_REVERSAL -> new ReversalTransactionBuilder(transaction.getCustomerEmail());
		};

		builder.withCustomerPhone(transaction.getCustomerPhone()).withReferenceTransaction(reference).withMerchant(merchant);
		if (reference != null && !reference.getStatus().isAllowReference()) {
			builder.withStatus(TransactionStatus.TRANSACTION_ERROR);
		}

		return builder.build();
	}
}
