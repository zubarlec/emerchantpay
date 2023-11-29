package com.emerchantpay.backend.web;

import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.emerchantpay.backend.domain.account.Admin;
import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.account.MerchantStatus;
import com.emerchantpay.backend.domain.builder.account.AdminBuilder;
import com.emerchantpay.backend.domain.builder.account.MerchantBuilder;
import com.emerchantpay.backend.dto.account.MerchantDTO;
import com.emerchantpay.backend.web.configuration.WebConfig;
import com.emerchantpay.backend.web.dto.merchant.UpdateMerchantRequestDTO;

public class MerchantControllerIntegrationTest extends BaseIntegrationTest {

	private Admin admin;
	private Merchant merchant1;
	private Merchant merchant2;

	@Override
	protected void init() {
		super.init();

		admin = new AdminBuilder("admin@test.com").withPassword(passwordEncoder.encode("pass")).build();
		merchant1 = new MerchantBuilder("merchant1@test.com").withName("name").withDescription("description").withTotalTransactionSum(new BigDecimal("1.01")).build();
		merchant2 = new MerchantBuilder("merchant2@test.com").withStatus(MerchantStatus.MERCHANT_INACTIVE).build();
	}

	@Test
	void get_all_merchants() {

		withAuthentication(admin, "pass")
			.when()
			.get(WebConfig.PREFIX + "/merchants/")
			.then()
			.statusCode(200)
			.body("value.id", contains(merchant1.getId().intValue(), merchant2.getId().intValue()))
			.body("value.email", contains(merchant1.getEmail(), merchant2.getEmail()))
			.body("value.status", contains(merchant1.getStatus().toString(), merchant2.getStatus().toString()))
			.body("value.findAll {it.name != null}.name", contains(merchant1.getName()))
			.body("value.findAll {it.description != null}.description", contains(merchant1.getDescription()))
			.body("value.findAll {it.totalTransactionSum != null}.totalTransactionSum", contains(merchant1.getTotalTransactionSum()));
	}

	@Test
	void get_one_merchant() {
		withAuthentication(admin, "pass")
			.when()
			.get(WebConfig.PREFIX + "/merchants/" + merchant1.getId())
			.then()
			.statusCode(200)
			.body("id", equalTo(merchant1.getId().intValue()))
			.body("status", equalTo(merchant1.getStatus().toString()))
			.body("name", equalTo(merchant1.getName()))
			.body("description", equalTo(merchant1.getDescription()))
			.body("totalTransactionSum", equalTo(merchant1.getTotalTransactionSum()));
	}

	@Test
	void create_merchant() {
		MerchantDTO merchantDTO = new MerchantDTO();
		merchantDTO.setEmail("new-merchant@test.com");
		merchantDTO.setName("new-merchant");
		merchantDTO.setDescription("new-merchant description");

		UpdateMerchantRequestDTO updateRequest = new UpdateMerchantRequestDTO();
		updateRequest.setMerchant(merchantDTO);

		withAuthentication(admin, "pass")
			.contentType(MediaType.APPLICATION_JSON)
			.body(updateRequest)
			.when()
			.post(WebConfig.PREFIX + "/merchants/")
			.then()
			.statusCode(200)
			.body("id", notNullValue())
			.body("status", equalTo(MerchantStatus.MERCHANT_ACTIVE.toString()))
			.body("name", equalTo(merchantDTO.getName()))
			.body("description", equalTo(merchantDTO.getDescription()))
			.body("totalTransactionSum", nullValue());
	}

	@Test
	void update_merchant() {
		MerchantDTO merchantDTO = new MerchantDTO(merchant1);
		merchantDTO.setName("updated name");

		UpdateMerchantRequestDTO updateRequest = new UpdateMerchantRequestDTO();
		updateRequest.setMerchant(merchantDTO);

		withAuthentication(admin, "pass")
			.contentType(MediaType.APPLICATION_JSON)
			.body(updateRequest)
			.when()
			.patch(WebConfig.PREFIX + "/merchants/")
			.then()
			.statusCode(200)
			.body("id", equalTo(merchant1.getId().intValue()))
			.body("status", equalTo(merchant1.getStatus().toString()))
			.body("name", equalTo(merchantDTO.getName()))
			.body("description", equalTo(merchant1.getDescription()))
			.body("totalTransactionSum", equalTo(merchant1.getTotalTransactionSum()));
	}

	@Test
	void delete_merchant() {
		withAuthentication(admin, "pass")
			.when()
			.delete(WebConfig.PREFIX + "/merchants/" + merchant1.getId())
			.then()
			.statusCode(200)
			.body("value", equalTo(true));
	}
}
