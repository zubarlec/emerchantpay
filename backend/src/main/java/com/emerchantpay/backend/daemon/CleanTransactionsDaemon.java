package com.emerchantpay.backend.daemon;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emerchantpay.backend.repository.RepositoryRegistry;

@Service
public class CleanTransactionsDaemon {
	public static final int MILLIS_PER_HOUR = 60 * 60 * 1000; //60min * 60sec * 1000ms

	private final RepositoryRegistry repo;

	public CleanTransactionsDaemon(RepositoryRegistry repo) {
		this.repo = repo;
	}

	@Transactional
	@Scheduled(cron = "0 0 * * * *") // sec min hour day-of-month month day-of-week
	void deleteExpiredTransactions() {
		repo.transaction.deleteByTimestampLessThan(System.currentTimeMillis() - MILLIS_PER_HOUR);
	}

}
