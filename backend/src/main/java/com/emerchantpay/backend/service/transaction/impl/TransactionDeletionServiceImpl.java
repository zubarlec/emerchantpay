package com.emerchantpay.backend.service.transaction.impl;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emerchantpay.backend.repository.RepositoryRegistry;
import com.emerchantpay.backend.service.transaction.TransactionDeletionService;

@Service
public class TransactionDeletionServiceImpl implements TransactionDeletionService {
	public static final int MILLIS_PER_HOUR = 60 * 60 * 1000; //60min * 60sec * 1000ms

	private final RepositoryRegistry repo;

	public TransactionDeletionServiceImpl(RepositoryRegistry repo) {
		this.repo = repo;
	}

	@Transactional
	@Override
	public void deleteExpiredTransactions() {
		long timestamp = System.currentTimeMillis() - MILLIS_PER_HOUR;
		Set<Long> affectedMerchantIds = repo.transaction.findMerchantIdsByTimestampLessThan(timestamp);
		repo.transaction.deleteByTimestampLessThan(timestamp);

		repo.merchant.updateTotalSum(affectedMerchantIds);
	}
}
