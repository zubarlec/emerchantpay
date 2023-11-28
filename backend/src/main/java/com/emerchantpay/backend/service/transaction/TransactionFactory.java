package com.emerchantpay.backend.service.transaction;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.transaction.Transaction;
import com.emerchantpay.backend.dto.transaction.TransactionDTO;

public interface TransactionFactory {
	Transaction createTransaction(TransactionDTO transaction, Transaction reference, Merchant merchant);
}
