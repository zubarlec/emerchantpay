package com.emerchantpay.backend.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.emerchantpay.backend.domain.account.Account;
import com.emerchantpay.backend.dto.transaction.TransactionDTO;
import com.emerchantpay.backend.service.exception.EntityNotFoundException;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;
import com.emerchantpay.backend.service.exception.InvalidTransactionException;
import com.emerchantpay.backend.service.transaction.TransactionService;
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

	@GetMapping("/")
	public ResponseEntity<ListWrapper<TransactionDTO>> getAll() {
		Account executor = authenticationService.getAuthenticatedAccount();

		return ResponseEntity.ok(new ListWrapper<>(transactionService.getTransactions(executor)));
	}

	@PostMapping("/")
	public ResponseEntity<TransactionDTO> submitTransaction(@NotNull @RequestBody TransactionDTO transaction) throws InvalidMerchantException, InvalidTransactionException, EntityNotFoundException {
		Account executor = authenticationService.getAuthenticatedAccount();

		return ResponseEntity.ok(transactionService.submitTransaction(transaction, executor));
	}

}
