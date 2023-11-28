package com.emerchantpay.backend.service.account;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.emerchantpay.backend.BaseTest;
import com.emerchantpay.backend.domain.account.AccountRole;
import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.account.MerchantStatus;
import com.emerchantpay.backend.domain.builder.account.MerchantBuilder;
import com.emerchantpay.backend.domain.builder.transaction.AuthorizeTransactionBuilder;
import com.emerchantpay.backend.dto.account.MerchantDTO;
import com.emerchantpay.backend.service.exception.DuplicateMailException;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;

public class MerchantCrudServiceTest extends BaseTest {

	@Autowired
	private MerchantCrudService merchantCrudService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private MerchantDTO merchantDTO;

	@Override
	protected void init() {
		merchantDTO = new MerchantDTO();
		merchantDTO.setEmail("merchant@test.com");
		merchantDTO.setName("Merchant Name");
		merchantDTO.setDescription("Merchant description");
	}

	@Test
	void create_merchant_all_fields() throws Exception {
		MerchantDTO result = merchantCrudService.createMerchant(merchantDTO, "pass");

		assertThat(result, notNullValue());
		assertThat(result.getId(), notNullValue());
		assertThat(result.getEmail(), equalTo(merchantDTO.getEmail()));
		assertThat(result.getRole(), equalTo(AccountRole.ROLE_MERCHANT));
		assertThat(result.getStatus(), equalTo(MerchantStatus.MERCHANT_ACTIVE));
		assertThat(result.getTotalTransactionSum(), nullValue());
		assertThat(result.getName(), equalTo(merchantDTO.getName()));
		assertThat(result.getDescription(), equalTo(merchantDTO.getDescription()));

		assertPassword(result.getId(), "pass");
	}

	@Test
	void create_merchant_only_email() throws Exception {
		merchantDTO = new MerchantDTO();
		merchantDTO.setEmail("merchant@test.com");

		MerchantDTO result = merchantCrudService.createMerchant(merchantDTO, null);

		assertThat(result, notNullValue());
		assertThat(result.getEmail(), equalTo(merchantDTO.getEmail()));
		assertThat(result.getName(), nullValue());
		assertThat(result.getDescription(), nullValue());
	}

	@Test
	void fail_to_create_merchant_with_invalid_email() throws Exception {
		merchantCrudService.createMerchant(merchantDTO, null);

		assertThrows(DuplicateMailException.class, () -> merchantCrudService.createMerchant(merchantDTO, null));

		merchantDTO.setEmail(null);
		assertThrows(InvalidMerchantException.class, () -> merchantCrudService.createMerchant(merchantDTO, null));

		merchantDTO.setEmail("");
		assertThrows(InvalidMerchantException.class, () -> merchantCrudService.createMerchant(merchantDTO, null));

		merchantDTO.setEmail("invalid-email");
		assertThrows(InvalidMerchantException.class, () -> merchantCrudService.createMerchant(merchantDTO, null));
	}

	@Test
	void fail_to_create_merchant_with_id() throws Exception {
		MerchantDTO existing = merchantCrudService.createMerchant(merchantDTO, "pass");

		merchantDTO.setId(existing.getId());
		merchantDTO.setEmail("another@test.com");
		assertThrows(InvalidMerchantException.class, () -> merchantCrudService.createMerchant(merchantDTO, null));
	}

	@Test
	void update_merchant_email() throws Exception {
		assertUpdateMerchantSingleField(MerchantDTO::getEmail, MerchantDTO::setEmail, "new-email@test.com");
	}

	@Test
	void update_merchant_name() throws Exception {
		assertUpdateMerchantSingleField(MerchantDTO::getName, MerchantDTO::setName, "new name");
	}

	@Test
	void update_merchant_description() throws Exception {
		assertUpdateMerchantSingleField(MerchantDTO::getDescription, MerchantDTO::setDescription, "new description");
	}

	@Test
	void update_merchant_status() throws Exception {
		assertUpdateMerchantSingleField(MerchantDTO::getStatus, MerchantDTO::setStatus, MerchantStatus.MERCHANT_INACTIVE);
	}

	@Test
	void update_merchant_password() throws Exception {
		MerchantDTO result = merchantCrudService.createMerchant(merchantDTO, "pass");
		result = merchantCrudService.updateMerchant(result, "new pass");

		assertPassword(result.getId(), "new pass");
	}

	@Test
	void fail_to_update_merchant_without_id() {
		assertThrows(InvalidMerchantException.class, () -> merchantCrudService.updateMerchant(merchantDTO, null));
	}

	@Test
	void get_merchants() {
		new MerchantBuilder("merchant1@test.com").withName("name").withDescription("description").withTotalTransactionSum(new BigDecimal("1.01")).build();
		new MerchantBuilder("merchant2@test.com").build();

		List<MerchantDTO> result = merchantCrudService.getMerchants();

		assertThat(result, hasSize(2));
		assertThat(result.get(0).getEmail(), equalTo("merchant1@test.com"));
		assertThat(result.get(1).getEmail(), equalTo("merchant2@test.com"));
	}

	@Test
	void getMerchant() throws Exception {
		new MerchantBuilder("merchant1@test.com").withName("name").withDescription("description").withTotalTransactionSum(new BigDecimal("1.01")).build();
		Merchant merchant2 = new MerchantBuilder("merchant2@test.com").build();

		MerchantDTO result = merchantCrudService.getMerchant(merchant2.getId());

		assertThat(result, notNullValue());
		assertThat(result.getEmail(), equalTo("merchant2@test.com"));
	}

	@Test
	void delete_merchant() throws Exception {
		Merchant merchant = new MerchantBuilder("merchant1@test.com").build();
		new MerchantBuilder("merchant2@test.com").build();

		long initialCount = repo.merchant.count();

		merchantCrudService.deleteMerchant(merchant.getId());

		assertThat(repo.merchant.count(), equalTo(initialCount - 1));
	}

	@Test
	void fail_to_delete_merchant_with_transactions() {
		Merchant merchant = new MerchantBuilder("merchant1@test.com").build();
		new AuthorizeTransactionBuilder(new BigDecimal("1.00"), "cusotmer@test.com", merchant).build();

		long initialCount = repo.merchant.count();

		assertThrows(InvalidMerchantException.class, () -> merchantCrudService.deleteMerchant(merchant.getId()));

		assertThat(repo.merchant.count(), equalTo(initialCount));
	}

	private <T> void assertUpdateMerchantSingleField(Function<MerchantDTO, T> fieldGetter, BiConsumer<MerchantDTO, T> fieldSetter, T newValue) throws Exception {
		MerchantDTO result = merchantCrudService.createMerchant(merchantDTO, null);

		MerchantDTO updateRequest = new MerchantDTO();
		updateRequest.setId(result.getId());
		fieldSetter.accept(updateRequest, newValue);

		MerchantDTO updated = merchantCrudService.updateMerchant(updateRequest, null);

		fieldSetter.accept(result, fieldGetter.apply(updated));

		assertThat(updated.getId(), equalTo(result.getId()));
		assertThat(updated.getEmail(), equalTo(result.getEmail()));
		assertThat(updated.getRole(), equalTo(result.getRole()));
		assertThat(updated.getStatus(), equalTo(result.getStatus()));
		assertThat(updated.getTotalTransactionSum(), equalTo(result.getTotalTransactionSum()));
		assertThat(updated.getName(), equalTo(result.getName()));
		assertThat(updated.getDescription(), equalTo(result.getDescription()));
	}

	private void assertPassword(Long merchantId, String expectedPass) {
		Merchant merchant = repo.merchant.findById(merchantId).orElseThrow();

		assertThat(passwordEncoder.matches(expectedPass, merchant.getPassword()), equalTo(true));
	}

}
