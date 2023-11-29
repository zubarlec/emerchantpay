package com.emerchantpay.backend.web;

import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.builder.account.MerchantBuilder;
import com.emerchantpay.backend.domain.builder.transaction.AuthorizeTransactionBuilder;
import com.emerchantpay.backend.domain.builder.transaction.ChargeTransactionBuilder;
import com.emerchantpay.backend.domain.transaction.AuthorizeTransaction;
import com.emerchantpay.backend.domain.transaction.ChargeTransaction;
import com.emerchantpay.backend.domain.transaction.TransactionStatus;
import com.emerchantpay.backend.domain.transaction.TransactionType;
import com.emerchantpay.backend.dto.transaction.TransactionDTO;
import com.emerchantpay.backend.web.configuration.WebConfig;

public class TransactionControllerIntegrationTest extends BaseIntegrationTest {

	private Merchant merchant;

	@Override
	protected void init() {
		super.init();
		merchant = new MerchantBuilder("merchant@test.com").withPassword(passwordEncoder.encode("pass")).build();
	}

	@Test
	void get_all_transactions() {
		AuthorizeTransaction authorize = new AuthorizeTransactionBuilder(new BigDecimal("10.01"), "customer@test.com", merchant).withCustomerPhone("+123456").build();
		ChargeTransaction charge = new ChargeTransactionBuilder(new BigDecimal("10.01"), "customer@test.com", merchant).withReferenceTransaction(authorize).build();

		withAuthentication(merchant, "pass")
			.when()
			.get(WebConfig.PREFIX + "/transaction/")
			.then()
			.statusCode(200)
			.body("value.id", contains(charge.getId().intValue(), authorize.getId().intValue()))
			.body("value.uuid", contains(charge.getUuid(), authorize.getUuid()))
			.body("value.status", contains(charge.getStatus().toString(), authorize.getStatus().toString()))
			.body("value.timestamp", contains(charge.getTimestamp(), authorize.getTimestamp()))
			.body("value.customerEmail", contains(charge.getCustomerEmail(), authorize.getCustomerEmail()))
			.body("value.customerPhone", contains(charge.getCustomerPhone(), authorize.getCustomerPhone()))
			.body("value.referenceId", contains(charge.getReferenceTransaction().getId().intValue(), null))
			.body("value.type", contains(TransactionType.TRANSACTION_CHARGE.toString(), TransactionType.TRANSACTION_AUTHORIZE.toString()))
			.body("value.merchant.id", contains(merchant.getId().intValue(), merchant.getId().intValue()))
			.body("value.amount", contains(charge.getAmount(), authorize.getAmount()));
	}

	@Test
	void submit_transaction() {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setType(TransactionType.TRANSACTION_AUTHORIZE);
		transactionDTO.setAmount(new BigDecimal("12.34"));
		transactionDTO.setCustomerEmail("customer@test.com");
		transactionDTO.setCustomerPhone("+123456");

		withAuthentication(merchant, "pass")
			.contentType(MediaType.APPLICATION_JSON)
			.body(transactionDTO)
			.when()
			.post(WebConfig.PREFIX + "/transaction/")
			.then()
			.statusCode(200)
			.body("id", notNullValue())
			.body("uuid", notNullValue())
			.body("status", equalTo(TransactionStatus.TRANSACTION_APPROVED.toString()))
			.body("timestamp", notNullValue())
			.body("customerEmail", equalTo(transactionDTO.getCustomerEmail()))
			.body("customerPhone", equalTo(transactionDTO.getCustomerPhone()))
			.body("referenceId", nullValue())
			.body("type", equalTo(TransactionType.TRANSACTION_AUTHORIZE.toString()))
			.body("merchant.id", equalTo(merchant.getId().intValue()))
			.body("amount", equalTo(transactionDTO.getAmount()));
	}
}
