package com.emerchantpay.backend.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.emerchantpay.backend.configuration.ConfigurationProperties;
import com.emerchantpay.backend.domain.account.Account;
import com.emerchantpay.backend.dto.transaction.TransactionDTO;
import com.emerchantpay.backend.service.exception.EntityNotFoundException;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;
import com.emerchantpay.backend.service.exception.InvalidTransactionException;
import com.emerchantpay.backend.service.transaction.TransactionService;
import com.emerchantpay.backend.util.JsonUtil;
import com.emerchantpay.backend.web.dto.ListWrapper;
import com.emerchantpay.backend.web.security.AuthenticationService;

import jakarta.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping(value = "transaction")
public class TransactionController {

	private final AuthenticationService authenticationService;
	private final TransactionService transactionService;

	public TransactionController(AuthenticationService authenticationService, TransactionService transactionService) {
		this.authenticationService = authenticationService;
		this.transactionService = transactionService;
	}

	/**
	 * Retrieve all transactions for the authenticated account.
	 * <ul>
	 *     <li>If the authenticated user is admin, all transactions are returned.</li>
	 *     <li>If the authenticated user is a merchant, all transactions that belong to that merchant are returned.</li>
	 *     <li>The resulting transactions are sorted by timestamp in descending order for merchants.</li>
	 * </ul>
	 *
	 * @return a ResponseEntity containing a ListWrapper of TransactionDTOs
	 */
	@GetMapping("/")
	public ResponseEntity<ListWrapper<TransactionDTO>> getAll() {
		Account executor = authenticationService.getAuthenticatedAccount();

		return ResponseEntity.ok(new ListWrapper<>(transactionService.getTransactions(executor)));
	}

	/**
	 * Submit a transaction for processing.
	 * <ul>
	 *     <li>If the authenticated user is admin, a {@code merchant.id} is required to be provided.</li>
	 *     <li>If the authenticated user is a merchant, the transaction is created for the merchant.</li>
	 * </ul>
	 *
	 * @param transaction the TransactionDTO object representing the transaction to be submitted
	 * @return a ResponseEntity containing the TransactionDTO object representing the submitted transaction
	 * @throws InvalidMerchantException if the merchant is invalid
	 * @throws InvalidTransactionException if the transaction is invalid
	 * @throws EntityNotFoundException if the reference transaction is not found
	 */
	@PostMapping("/")
	public ResponseEntity<TransactionDTO> submitTransaction(@NotNull @RequestBody TransactionDTO transaction) throws InvalidMerchantException, InvalidTransactionException, EntityNotFoundException {
		Account executor = authenticationService.getAuthenticatedAccount();

		ConfigurationProperties.LOG.info(String.format("Submit transaction: %s, executor: %d", JsonUtil.toJson(transaction), executor.getId()));

		return ResponseEntity.ok(transactionService.submitTransaction(transaction, executor));
	}

}
