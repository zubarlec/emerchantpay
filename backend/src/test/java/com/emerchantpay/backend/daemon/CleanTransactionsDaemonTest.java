package com.emerchantpay.backend.daemon;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.emerchantpay.backend.BaseTest;
import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.builder.account.MerchantBuilder;
import com.emerchantpay.backend.domain.builder.transaction.ChargeTransactionBuilder;
import com.emerchantpay.backend.domain.transaction.Transaction;

public class CleanTransactionsDaemonTest extends BaseTest {

	@Autowired
	private CleanTransactionsDaemon cleanTransactionsDaemon;

	@Test
	void delete_old_transactions() {
		Merchant merchant = new MerchantBuilder("merchant@text.com").build();
		Transaction t1 = new ChargeTransactionBuilder(new BigDecimal("1.0"), "test@test.com", merchant).build();
		Transaction t2 = new ChargeTransactionBuilder(new BigDecimal("2.0"), "test@test.com", merchant).withTimestamp(Instant.now().minus(59, ChronoUnit.MINUTES).toEpochMilli()).build();
		Transaction t3 = new ChargeTransactionBuilder(new BigDecimal("4.0"), "test@test.com", merchant).withTimestamp(Instant.now().minus(1, ChronoUnit.HOURS).toEpochMilli()).build();
		Transaction t4 = new ChargeTransactionBuilder(new BigDecimal("8.0"), "test@test.com", merchant).withTimestamp(Instant.now().minus(2, ChronoUnit.HOURS).toEpochMilli()).build();

		long initialCount = repo.transaction.count();

		cleanTransactionsDaemon.deleteExpiredTransactions();

		assertThat(repo.transaction.count(), equalTo(initialCount - 2));
		assertThat(repo.transaction.findById(t1.getId()).orElse(null), notNullValue());
		assertThat(repo.transaction.findById(t2.getId()).orElse(null), notNullValue());
		assertThat(repo.transaction.findById(t3.getId()).orElse(null), nullValue());
		assertThat(repo.transaction.findById(t4.getId()).orElse(null), nullValue());

		merchant = repo.merchant.findById(merchant.getId()).orElseThrow();
		assertThat(merchant.getTotalTransactionSum(), equalTo(new BigDecimal("3.00")));

		cleanTransactionsDaemon.deleteExpiredTransactions();
		assertThat(repo.transaction.count(), equalTo(initialCount - 2));
	}
}
