package com.emerchantpay.backend.service.account;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.builder.account.MerchantBuilder;

@Service
public class MerchantTotalSumService {

	public void addToTotalSum(Merchant merchant, BigDecimal value) {
		updateTotalSum(merchant, value, BigDecimal::add);
	}

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
