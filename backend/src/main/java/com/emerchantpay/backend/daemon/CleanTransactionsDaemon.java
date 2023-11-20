package com.emerchantpay.backend.daemon;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emerchantpay.backend.configuration.ConfigurationProperties;
import com.emerchantpay.backend.repository.RepositoryRegistry;

@Service
public class CleanTransactionsDaemon {
	public static final int MILLIS_PER_HOUR = 60 * 60 * 1000; //60min * 60sec * 1000ms

	private final RepositoryRegistry repo;

	public CleanTransactionsDaemon(RepositoryRegistry repo) {
		this.repo = repo;
	}

	@Transactional
	@Scheduled(cron = "0 */30 * * * *") // "sec min hour day-of-month month day-of-week" - runs every 30 minutes
	void deleteExpiredTransactions() {
		ConfigurationProperties.LOG.info("CleanTransactionsDaemon run.");

		long timestamp = System.currentTimeMillis() - MILLIS_PER_HOUR;
		Set<Long> affectedMerchantIds = repo.transaction.findMerchantIdsByTimestampLessThan(timestamp);
		repo.transaction.deleteByTimestampLessThan(timestamp);

		repo.merchant.updateTotalSum(affectedMerchantIds);
	}

}
