package com.emerchantpay.backend.service.transaction;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emerchantpay.backend.domain.account.Account;
import com.emerchantpay.backend.domain.account.Admin;
import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.account.MerchantStatus;
import com.emerchantpay.backend.domain.builder.transaction.TransactionBuilder;
import com.emerchantpay.backend.domain.transaction.*;
import com.emerchantpay.backend.dto.transaction.TransactionDTO;
import com.emerchantpay.backend.repository.RepositoryRegistry;
import com.emerchantpay.backend.service.account.MerchantTotalSumService;
import com.emerchantpay.backend.service.exception.EntityNotFoundException;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;
import com.emerchantpay.backend.service.exception.InvalidTransactionException;

@Service
public class TransactionService {

	private final RepositoryRegistry repo;
	private final TransactionFactory transactionFactory;
	private final MerchantTotalSumService merchantTotalSumService;

	public TransactionService(RepositoryRegistry repo, TransactionFactory transactionFactory, MerchantTotalSumService merchantTotalSumService) {
		this.repo = repo;
		this.transactionFactory = transactionFactory;
		this.merchantTotalSumService = merchantTotalSumService;
	}

	@Transactional
	public TransactionDTO submitTransaction(TransactionDTO transactionDTO, Account executor) throws EntityNotFoundException, InvalidTransactionException, InvalidMerchantException {
		if (transactionDTO.getType() == null) {
			throw new InvalidTransactionException("Empty type");
		}

		Merchant merchant = null;
		if (executor instanceof Merchant) {
			merchant = (Merchant) executor;
		} else if (executor instanceof Admin && transactionDTO.getMerchant() != null && transactionDTO.getMerchant().getId() != null) {
			merchant = repo.merchant.findById(transactionDTO.getMerchant().getId()).orElseThrow(InvalidMerchantException::new);
		}

		if (merchant == null || merchant.getStatus() != MerchantStatus.MERCHANT_ACTIVE) {
			throw new InvalidMerchantException();
		}

		Transaction referenceTransaction = null;
		if (transactionDTO.getReferenceId() != null) {
			referenceTransaction = repo.transaction.findById(transactionDTO.getReferenceId()).orElseThrow(() -> new EntityNotFoundException("reference transaction"));
		}

		if (!transactionDTO.getType().canReference(TransactionType.getType(referenceTransaction))) {
			throw new InvalidTransactionException("Transaction type not allowed to refer the reference transaction.");
		}

		Transaction result = transactionFactory.createTransaction(transactionDTO, referenceTransaction, merchant);

		if (result.getStatus() == TransactionStatus.TRANSACTION_APPROVED) {
			if (result instanceof ChargeTransaction) {
				merchantTotalSumService.addToTotalSum(merchant, ((ChargeTransaction) result).getAmount());
			} else if (result instanceof RefundTransaction) {
				new TransactionBuilder(referenceTransaction).withStatus(TransactionStatus.TRANSACTION_REFUNDED).build();
				merchantTotalSumService.subtractFromTotalSum(merchant, ((RefundTransaction) result).getAmount());
			} else if (result instanceof ReversalTransaction) {
				new TransactionBuilder(referenceTransaction).withStatus(TransactionStatus.TRANSACTION_REVERSED).build();
			}
		}

		return new TransactionDTO(result);
	}

	public List<TransactionDTO> getTransactions(Account executor) {
		List<TransactionDTO> result;
		if (executor instanceof Merchant) {
			result = ((Merchant) executor).getTransactions().stream().map(TransactionDTO::new).toList();
		} else {
			result = repo.transaction.findAll(Sort.by("id")).stream().map(TransactionDTO::new).toList();
		}
		return result;
	}

}
