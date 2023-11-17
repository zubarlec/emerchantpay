package com.emerchantpay.backend.service.account;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.emerchantpay.backend.BaseTest;
import com.emerchantpay.backend.domain.account.Admin;
import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.account.MerchantStatus;

public class ImportAccountsServiceTest extends BaseTest {

	@Autowired
	private ImportAccountsService importAccountsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void import_accounts() throws Exception {
		InputStream is = new ClassPathResource("test-accounts.csv").getInputStream();

		importAccountsService.importFromCsv(is);

		assertThat(repo.account.count(), equalTo(3L));
		assertThat(repo.admin.count(), equalTo(1L));
		assertThat(repo.merchant.count(), equalTo(2L));

		Admin admin = repo.admin.findAll().get(0);
		assertThat(admin.getEmail(), equalTo("admin@test.com"));
		assertThat(admin.getPassword(), notNullValue());
		assertThat(passwordEncoder.matches("adminpass", admin.getPassword()), equalTo(true));

		List<Merchant> merchants = repo.merchant.findAll(Sort.by("id"));
		assertThat(merchants.get(0).getEmail(), equalTo("merchant@test.com"));
		assertThat(merchants.get(0).getName(), equalTo("Merchant Name"));
		assertThat(merchants.get(0).getDescription(), equalTo("Some merchant description"));
		assertThat(merchants.get(0).getStatus(), equalTo(MerchantStatus.MERCHANT_ACTIVE));

		assertThat(merchants.get(1).getStatus(), equalTo(MerchantStatus.MERCHANT_INACTIVE));
	}

}
