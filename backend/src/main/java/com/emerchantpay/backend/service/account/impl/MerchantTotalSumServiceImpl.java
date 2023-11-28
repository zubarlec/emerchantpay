package com.emerchantpay.backend.service.account.impl;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.builder.account.MerchantBuilder;
import com.emerchantpay.backend.service.account.MerchantTotalSumService;

@Service
public class MerchantTotalSumServiceImpl implements MerchantTotalSumService {

	@Override
	public void addToTotalSum(Merchant merchant, BigDecimal value) {
		updateTotalSum(merchant, value, BigDecimal::add);
	}

	@Override
	public void subtractFromTotalSum(Merchant merchant, BigDecimal value) {
		updateTotalSum(merchant, value, BigDecimal::subtract);
	}

	private void updateTotalSum(Merchant merchant, BigDecimal value, BiFunction<BigDecimal, BigDecimal, BigDecimal> operation) {
		if (value == null) {
			return;
		}
		BigDecimal sum = merchant.getTotalTransactionSum();
		if (sum == null) {
			sum = value;
		} else {
			sum = operation.apply(sum, value);
		}
		new MerchantBuilder(merchant).withTotalTransactionSum(sum).build();
	}
}
