package com.emerchantpay.backend.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.emerchantpay.backend.BaseTest;
import com.emerchantpay.backend.domain.builder.transaction.AuthorizeTransactionBuilder;
import com.emerchantpay.backend.domain.builder.transaction.ChargeTransactionBuilder;
import com.emerchantpay.backend.domain.builder.transaction.RefundTransactionBuilder;
import com.emerchantpay.backend.domain.builder.transaction.ReversalTransactionBuilder;
import com.emerchantpay.backend.domain.transaction.AuthorizeTransaction;
import com.emerchantpay.backend.domain.transaction.ChargeTransaction;
import com.emerchantpay.backend.domain.transaction.TransactionStatus;

import jakarta.validation.ConstraintViolationException;

public class TransactionTest extends BaseTest {

	@Test
	void create_transactions() {
		AuthorizeTransaction authorizeTransaction = new AuthorizeTransactionBuilder(new BigDecimal("5.5"), "test@test.com").withCustomerPhone("1234567").build();

		assertThat(authorizeTransaction, notNullValue());
		assertThat(authorizeTransaction.getId(), notNullValue());
		assertThat(authorizeTransaction.getUuid(), notNullValue());
		assertThat((double)authorizeTransaction.getTimestamp(), closeTo(System.currentTimeMillis(), 100));
		assertThat(authorizeTransaction.getStatus(), equalTo(TransactionStatus.TRANSACTION_APPROVED));
		assertThat(authorizeTransaction.getCustomerEmail(), equalTo("test@test.com"));
		assertThat(authorizeTransaction.getCustomerPhone(), equalTo("1234567"));

		ChargeTransaction chargeTransaction = new ChargeTransactionBuilder(new BigDecimal("4.51"), "test@test.com").withReferenceTransaction(authorizeTransaction).build();

		assertThat(chargeTransaction.getReferenceTransaction(), notNullValue());
		assertThat(chargeTransaction.getReferenceTransaction().getId(), equalTo(authorizeTransaction.getId()));

		new RefundTransactionBuilder(new BigDecimal("5.52"), "test@test.com").withReferenceTransaction(chargeTransaction).build();

		authorizeTransaction = new AuthorizeTransactionBuilder(new BigDecimal("7.7"), "test@test.com").build();
		new ReversalTransactionBuilder("test@test.com").withReferenceTransaction(authorizeTransaction).build();

		assertThat(repo.transaction.count(), equalTo(5L));
		assertThat(repo.authorizeTransaction.count(), equalTo(2L));
		assertThat(repo.chargeTransaction.count(), equalTo(1L));
		assertThat(repo.refundTransaction.count(), equalTo(1L));
		assertThat(repo.reversalTransaction.count(), equalTo(1L));
	}

	@Test
	void fail_to_create_invalid_transaction() {
		ConstraintViolationException e = assertThrows(ConstraintViolationException.class, () -> new AuthorizeTransactionBuilder(new BigDecimal("0"), "test@test.com").build());
		assertThat(e.getConstraintViolations(), hasSize(1));
		assertThat(e.getConstraintViolations().iterator().next().getPropertyPath().toString(), equalTo("amount"));

		e = assertThrows(ConstraintViolationException.class, () -> new AuthorizeTransactionBuilder(null, "test@test.com").build());
		assertThat(e.getConstraintViolations(), hasSize(1));
		assertThat(e.getConstraintViolations().iterator().next().getPropertyPath().toString(), equalTo("amount"));

		e = assertThrows(ConstraintViolationException.class, () -> new AuthorizeTransactionBuilder(new BigDecimal("0.01"), null).build());
		assertThat(e.getConstraintViolations(), hasSize(1));
		assertThat(e.getConstraintViolations().iterator().next().getPropertyPath().toString(), equalTo("customerEmail"));

		e = assertThrows(ConstraintViolationException.class, () -> new AuthorizeTransactionBuilder(new BigDecimal("0.01"), "").build());
		assertThat(e.getConstraintViolations(), hasSize(1));
		assertThat(e.getConstraintViolations().iterator().next().getPropertyPath().toString(), equalTo("customerEmail"));

		e = assertThrows(ConstraintViolationException.class, () -> new AuthorizeTransactionBuilder(new BigDecimal("0.01"), "test@test.com").withStatus(null).build());
		assertThat(e.getConstraintViolations(), hasSize(1));
		assertThat(e.getConstraintViolations().iterator().next().getPropertyPath().toString(), equalTo("status"));
	}
}
