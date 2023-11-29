package com.emerchantpay.backend.web;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.emerchantpay.backend.domain.account.Admin;
import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.builder.account.AdminBuilder;
import com.emerchantpay.backend.domain.builder.account.MerchantBuilder;
import com.emerchantpay.backend.web.configuration.WebConfig;


public class LoginControllerIntegrationTest extends BaseIntegrationTest {


	@Test
	void post_login_invalid_credentials() {
		mvcSpec
			.formParam("username", "invalid").formParam("password", "invalid")
			.when()
			.post(WebConfig.PREFIX + "/auth/login")
			.then()
			.statusCode(400);
	}

	@Test
	void post_login_valid_credentials() {
		new AdminBuilder("admin@test.com").withPassword(passwordEncoder.encode("pass")).build();
		mvcSpec
			.formParam("username", "admin@test.com").formParam("password", "pass")
			.when()
			.post(WebConfig.PREFIX + "/auth/login")
			.then()
			.statusCode(200)
			.body("value", matchesPattern(".+\\..+\\..+"));
	}

	@Test
	void get_account_not_authenticated() {
		mvcSpec
			.when()
			.get(WebConfig.PREFIX + "/auth/account")
			.then()
			.statusCode(200)
			.body(equalTo(""));
	}

	@Test
	void get_account_authenticated_admin() {
		Admin admin = new AdminBuilder("admin@test.com").withPassword(passwordEncoder.encode("pass")).build();

		withAuthentication(admin, "pass")
			.when()
			.get(WebConfig.PREFIX + "/auth/account")
			.then()
			.statusCode(200)
			.body("id", equalTo(admin.getId().intValue()))
			.body("email", equalTo(admin.getEmail()))
			.body("role", equalTo(admin.getRole().toString()));
	}

	@Test
	void get_account_authenticated_merchant() {
		Merchant merchant = new MerchantBuilder("merchant@test.com").withDescription("description").withName("merchant name").withTotalTransactionSum(new BigDecimal("12.34")).withPassword(passwordEncoder.encode("pass")).build();

		withAuthentication(merchant, "pass")
			.when()
			.get(WebConfig.PREFIX + "/auth/account")
			.then()
			.statusCode(200)
			.body("name", equalTo(merchant.getName()))
			.body("description", equalTo(merchant.getDescription()))
			.body("status", equalTo(merchant.getStatus().toString()))
			.body("totalTransactionSum", equalTo(merchant.getTotalTransactionSum()));
	}
}
