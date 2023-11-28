package com.emerchantpay.backend.daemon;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.emerchantpay.backend.configuration.ConfigurationProperties;
import com.emerchantpay.backend.service.transaction.TransactionDeletionService;

@Service
public class CleanTransactionsDaemon {

	private final TransactionDeletionService transactionDeletionService;

	public CleanTransactionsDaemon(TransactionDeletionService transactionDeletionService) {
		this.transactionDeletionService = transactionDeletionService;
	}

	@Scheduled(cron = "0 */30 * * * *") // "sec min hour day-of-month month day-of-week" - runs every 30 minutes
	void deleteExpiredTransactions() {
		ConfigurationProperties.LOG.info("CleanTransactionsDaemon run.");
		transactionDeletionService.deleteExpiredTransactions();
	}

}
