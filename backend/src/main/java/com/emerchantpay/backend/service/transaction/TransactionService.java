package com.emerchantpay.backend.service.transaction;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.emerchantpay.backend.domain.account.Account;
import com.emerchantpay.backend.dto.transaction.TransactionDTO;
import com.emerchantpay.backend.service.exception.EntityNotFoundException;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;
import com.emerchantpay.backend.service.exception.InvalidTransactionException;

public interface TransactionService {
	@Transactional
	TransactionDTO submitTransaction(TransactionDTO transactionDTO, Account executor) throws EntityNotFoundException, InvalidTransactionException, InvalidMerchantException;

	@Transactional
	List<TransactionDTO> getTransactions(Account executor);
}
