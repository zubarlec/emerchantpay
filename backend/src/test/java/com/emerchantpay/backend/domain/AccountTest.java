package com.emerchantpay.backend.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.emerchantpay.backend.BaseTest;
import com.emerchantpay.backend.domain.account.AccountRole;
import com.emerchantpay.backend.domain.account.Admin;
import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.account.MerchantStatus;
import com.emerchantpay.backend.domain.builder.account.AdminBuilder;
import com.emerchantpay.backend.domain.builder.account.MerchantBuilder;

import jakarta.validation.ConstraintViolationException;

public class AccountTest extends BaseTest {

	@Test
	void create_accounts() {
		Admin admin = new AdminBuilder("admin@test.com").withPassword("encrypted-password").build();

		assertThat(admin, notNullValue());
		assertThat(admin.getId(), notNullValue());
		assertThat(admin.getEmail(), equalTo("admin@test.com"));
		assertThat(admin.getRole(), equalTo(AccountRole.ROLE_ADMIN));
		assertThat(admin.getPassword(), equalTo("encrypted-password"));

		Merchant merchant = new MerchantBuilder("merchant@test.com").withName("name").withDescription("description").withTotalTransactionSum(new BigDecimal("3.32")).build();

		assertThat(merchant, notNullValue());
		assertThat(merchant.getId(), notNullValue());
		assertThat(merchant.getName(), equalTo("name"));
		assertThat(merchant.getDescription(), equalTo("description"));
		assertThat(merchant.getRole(), equalTo(AccountRole.ROLE_MERCHANT));
		assertThat(merchant.getStatus(), equalTo(MerchantStatus.MERCHANT_ACTIVE));
		assertThat(merchant.getTotalTransactionSum(), equalTo(new BigDecimal("3.32")));

		assertThat(repo.admin.count(), equalTo(1L));
		assertThat(repo.merchant.count(), equalTo(1L));
	}

	@Test
	void fail_to_create_invalid_account() {
		ConstraintViolationException e = assertThrows(ConstraintViolationException.class, () -> new AdminBuilder(null).build());
		assertThat(e.getConstraintViolations(), hasSize(1));
		assertThat(e.getConstraintViolations().iterator().next().getPropertyPath().toString(), equalTo("email"));

		e = assertThrows(ConstraintViolationException.class, () -> new AdminBuilder("").build());
		assertThat(e.getConstraintViolations(), hasSize(1));
		assertThat(e.getConstraintViolations().iterator().next().getPropertyPath().toString(), equalTo("email"));

		e = assertThrows(ConstraintViolationException.class, () -> new AdminBuilder("invalid-email").build());
		assertThat(e.getConstraintViolations(), hasSize(1));
		assertThat(e.getConstraintViolations().iterator().next().getPropertyPath().toString(), equalTo("email"));
	}
}
