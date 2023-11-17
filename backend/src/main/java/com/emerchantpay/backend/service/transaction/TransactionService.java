package com.emerchantpay.backend.service.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.account.MerchantStatus;
import com.emerchantpay.backend.domain.builder.transaction.TransactionBuilder;
import com.emerchantpay.backend.domain.transaction.*;
import com.emerchantpay.backend.dto.transaction.TransactionDTO;
import com.emerchantpay.backend.repository.RepositoryRegistry;
import com.emerchantpay.backend.service.account.MerchantService;
import com.emerchantpay.backend.service.exception.EntityNotFoundException;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;
import com.emerchantpay.backend.service.exception.InvalidTransactionException;

@Service
public class TransactionService {

	private final RepositoryRegistry repo;
	private final TransactionFactory transactionFactory;
	private final MerchantService merchantService;

	public TransactionService(RepositoryRegistry repo, TransactionFactory transactionFactory, MerchantService merchantService) {
		this.repo = repo;
		this.transactionFactory = transactionFactory;
		this.merchantService = merchantService;
	}

	@Transactional
	public TransactionDTO submitTransaction(TransactionDTO transactionDTO, Merchant merchant) throws EntityNotFoundException, InvalidTransactionException, InvalidMerchantException {
		if (transactionDTO.getType() == null || merchant == null) {
			throw new InvalidTransactionException();
		}

		if (merchant.getStatus() != MerchantStatus.MERCHANT_ACTIVE) {
			throw new InvalidMerchantException();
		}

		Transaction referenceTransaction = null;
		if (transactionDTO.getReferenceId() != null) {
			referenceTransaction = repo.transaction.findById(transactionDTO.getReferenceId()).orElseThrow(EntityNotFoundException::new);
		}

		if (!transactionDTO.getType().canReference(TransactionType.getType(referenceTransaction))) {
			throw new InvalidTransactionException();
		}

		Transaction result = transactionFactory.createTransaction(transactionDTO, referenceTransaction, merchant);

		if (result.getStatus() == TransactionStatus.TRANSACTION_APPROVED) {
			if (result instanceof ChargeTransaction) {
				merchantService.addToTotalSum(merchant, ((ChargeTransaction) result).getAmount());
			} else if (result instanceof RefundTransaction) {
				new TransactionBuilder(referenceTransaction).withStatus(TransactionStatus.TRANSACTION_REFUNDED).build();
				merchantService.subtractFromTotalSum(merchant, ((RefundTransaction) result).getAmount());
			} else if (result instanceof ReversalTransaction) {
				new TransactionBuilder(referenceTransaction).withStatus(TransactionStatus.TRANSACTION_REVERSED).build();
			}
		}

		return new TransactionDTO(result);
	}
}
