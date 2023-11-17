package com.emerchantpay.backend.service.transaction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.emerchantpay.backend.BaseTest;
import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.account.MerchantStatus;
import com.emerchantpay.backend.domain.builder.account.MerchantBuilder;
import com.emerchantpay.backend.domain.builder.transaction.*;
import com.emerchantpay.backend.domain.transaction.Transaction;
import com.emerchantpay.backend.domain.transaction.TransactionStatus;
import com.emerchantpay.backend.domain.transaction.TransactionType;
import com.emerchantpay.backend.dto.transaction.TransactionDTO;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;
import com.emerchantpay.backend.service.exception.InvalidTransactionException;

public class TransactionServiceTest extends BaseTest {

	@Autowired
	private TransactionService transactionService;

	private TransactionDTO transactionDTO;
	private Merchant merchant;

	@Override
	protected void init() {
		transactionDTO = new TransactionDTO();
		transactionDTO.setAmount(new BigDecimal("12.34"));
		transactionDTO.setCustomerEmail("customer@test.com");

		merchant = new MerchantBuilder("merchant@test.com").build();
	}

	@Test
	void submit_authorize_transaction() throws Exception {
		transactionDTO.setType(TransactionType.TRANSACTION_AUTHORIZE);

		TransactionDTO result = transactionService.submitTransaction(transactionDTO, merchant);

		assertThat(result, notNullValue());
		assertThat(result.getCustomerEmail(), equalTo(transactionDTO.getCustomerEmail()));
		assertThat(result.getAmount(), equalTo(transactionDTO.getAmount()));
		assertThat(result.getType(), equalTo(transactionDTO.getType()));
		assertThat(result.getStatus(), equalTo(TransactionStatus.TRANSACTION_APPROVED));
	}

	@Test
	void submit_charge_transaction() throws Exception {
		transactionDTO.setType(TransactionType.TRANSACTION_AUTHORIZE);
		TransactionDTO authorize = transactionService.submitTransaction(transactionDTO, merchant);

		transactionDTO.setType(TransactionType.TRANSACTION_CHARGE);
		transactionDTO.setReferenceId(authorize.getId());
		TransactionDTO result = transactionService.submitTransaction(transactionDTO, merchant);

		assertThat(result, notNullValue());
		assertThat(result.getAmount(), equalTo(transactionDTO.getAmount()));
		assertThat(result.getStatus(), equalTo(TransactionStatus.TRANSACTION_APPROVED));

		merchant = repo.merchant.findById(merchant.getId()).orElseThrow();
		assertThat(merchant.getTotalTransactionSum(), equalTo(transactionDTO.getAmount()));
	}

	@Test
	void submit_refund_transaction() throws Exception {
		transactionDTO.setType(TransactionType.TRANSACTION_AUTHORIZE);
		TransactionDTO authorize = transactionService.submitTransaction(transactionDTO, merchant);

		transactionDTO.setType(TransactionType.TRANSACTION_CHARGE);
		transactionDTO.setReferenceId(authorize.getId());
		TransactionDTO charge = transactionService.submitTransaction(transactionDTO, merchant);

		transactionDTO.setType(TransactionType.TRANSACTION_REFUND);
		transactionDTO.setReferenceId(charge.getId());
		TransactionDTO result = transactionService.submitTransaction(transactionDTO, merchant);

		assertThat(result, notNullValue());
		assertThat(result.getAmount(), equalTo(transactionDTO.getAmount()));
		assertThat(result.getStatus(), equalTo(TransactionStatus.TRANSACTION_APPROVED));

		Transaction updatedCharge = repo.chargeTransaction.findById(charge.getId()).orElseThrow();
		assertThat(updatedCharge.getStatus(), equalTo(TransactionStatus.TRANSACTION_REFUNDED));

		merchant = repo.merchant.findById(merchant.getId()).orElseThrow();
		assertThat(merchant.getTotalTransactionSum(), equalTo(new BigDecimal("0.00")));
	}

	@Test
	void submit_reversal_transaction() throws Exception {
		transactionDTO.setType(TransactionType.TRANSACTION_AUTHORIZE);
		TransactionDTO authorize = transactionService.submitTransaction(transactionDTO, merchant);

		transactionDTO.setType(TransactionType.TRANSACTION_REVERSAL);
		transactionDTO.setReferenceId(authorize.getId());
		TransactionDTO result = transactionService.submitTransaction(transactionDTO, merchant);

		assertThat(result, notNullValue());
		assertThat(result.getAmount(), nullValue());
		assertThat(result.getStatus(), equalTo(TransactionStatus.TRANSACTION_APPROVED));

		Transaction updatedAuthorize = repo.authorizeTransaction.findById(authorize.getId()).orElseThrow();
		assertThat(updatedAuthorize.getStatus(), equalTo(TransactionStatus.TRANSACTION_REVERSED));
	}

	@Test
	void fail_to_submit_transaction_no_merchant() {
		transactionDTO.setType(TransactionType.TRANSACTION_AUTHORIZE);
		assertThrows(InvalidTransactionException.class, () -> transactionService.submitTransaction(transactionDTO, null));
	}

	@Test
	void fail_to_submit_transaction_not_active_merchant() {
		merchant = new MerchantBuilder(merchant).withStatus(MerchantStatus.MERCHANT_INACTIVE).build();

		transactionDTO.setType(TransactionType.TRANSACTION_AUTHORIZE);
		assertThrows(InvalidMerchantException.class, () -> transactionService.submitTransaction(transactionDTO, merchant));
	}

	@Test
	void fail_to_submit_transaction_no_type() {
		assertThrows(InvalidTransactionException.class, () -> transactionService.submitTransaction(transactionDTO, merchant));
	}

	@Test
	void fail_to_submit_transaction_no_reference() {

		transactionDTO.setType(TransactionType.TRANSACTION_CHARGE);
		assertThrows(InvalidTransactionException.class, () -> transactionService.submitTransaction(transactionDTO, merchant));

		transactionDTO.setType(TransactionType.TRANSACTION_REFUND);
		assertThrows(InvalidTransactionException.class, () -> transactionService.submitTransaction(transactionDTO, merchant));

		transactionDTO.setType(TransactionType.TRANSACTION_REVERSAL);
		assertThrows(InvalidTransactionException.class, () -> transactionService.submitTransaction(transactionDTO, merchant));
	}

	@Test
	void fail_to_submit_transaction_for_invalid_reference_type() {
		Transaction authorize = new AuthorizeTransactionBuilder(transactionDTO.getAmount(), transactionDTO.getCustomerEmail(), merchant).build();
		Transaction charge = new ChargeTransactionBuilder(transactionDTO.getAmount(), transactionDTO.getCustomerEmail(), merchant).build();
		Transaction refund = new RefundTransactionBuilder(transactionDTO.getAmount(), transactionDTO.getCustomerEmail(), merchant).build();
		Transaction reversal = new ReversalTransactionBuilder(transactionDTO.getCustomerEmail(), merchant).build();

		transactionDTO.setType(TransactionType.TRANSACTION_AUTHORIZE);
		List.of(authorize, charge, refund, reversal).forEach(t -> {
			transactionDTO.setReferenceId(t.getId());
			assertThrows(InvalidTransactionException.class, () -> transactionService.submitTransaction(transactionDTO, merchant));
		});

		transactionDTO.setType(TransactionType.TRANSACTION_CHARGE);
		List.of(charge, refund, reversal).forEach(t -> {
			transactionDTO.setReferenceId(t.getId());
			assertThrows(InvalidTransactionException.class, () -> transactionService.submitTransaction(transactionDTO, merchant));
		});

		transactionDTO.setType(TransactionType.TRANSACTION_REFUND);
		List.of(authorize, refund, reversal).forEach(t -> {
			transactionDTO.setReferenceId(t.getId());
			assertThrows(InvalidTransactionException.class, () -> transactionService.submitTransaction(transactionDTO, merchant));
		});

		transactionDTO.setType(TransactionType.TRANSACTION_REVERSAL);
		List.of(charge, refund, reversal).forEach(t -> {
			transactionDTO.setReferenceId(t.getId());
			assertThrows(InvalidTransactionException.class, () -> transactionService.submitTransaction(transactionDTO, merchant));
		});
	}

	@Test
	void submit_transaction_for_invalid_reference_status() {
		Transaction authorize = new AuthorizeTransactionBuilder(transactionDTO.getAmount(), transactionDTO.getCustomerEmail(), merchant).build();

		transactionDTO.setReferenceId(authorize.getId());
		List.of(TransactionStatus.TRANSACTION_ERROR, TransactionStatus.TRANSACTION_REVERSED).forEach(status -> {
			new TransactionBuilder(authorize).withStatus(status).build();

			List.of(TransactionType.TRANSACTION_CHARGE, TransactionType.TRANSACTION_REVERSAL).forEach(type -> {
				transactionDTO.setType(type);
				TransactionDTO result = assertDoesNotThrow(() -> transactionService.submitTransaction(transactionDTO, merchant));

				assertThat(result.getStatus(), equalTo(TransactionStatus.TRANSACTION_ERROR));
			});
		});
	}
}
