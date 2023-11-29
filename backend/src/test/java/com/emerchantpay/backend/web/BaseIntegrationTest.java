package com.emerchantpay.backend.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.emerchantpay.backend.BaseTest;
import com.emerchantpay.backend.domain.account.Account;
import com.emerchantpay.backend.web.security.AuthenticationService;

import io.restassured.config.JsonConfig;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.path.json.config.JsonPathConfig;

public abstract class BaseIntegrationTest extends BaseTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationService authenticationService;


	protected MockMvcRequestSpecification mvcSpec;

	@Override
	protected void init() {
		mvcSpec = given().mockMvc(mockMvc).config(RestAssuredMockMvcConfig.config().jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)));
	}

	protected MockMvcRequestSpecification withAuthentication(Account account, String pass) {
		String jwt = authenticationService.authenticate(account.getEmail(), pass);
		SecurityContextHolder.getContext().setAuthentication(null);

		return mvcSpec.header("Authorization", "Bearer " + jwt);
	}
}
