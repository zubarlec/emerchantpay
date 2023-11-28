package com.emerchantpay.backend.service.account;

import java.math.BigDecimal;

import com.emerchantpay.backend.domain.account.Merchant;

public interface MerchantTotalSumService {
	void addToTotalSum(Merchant merchant, BigDecimal value);

	void subtractFromTotalSum(Merchant merchant, BigDecimal value);
}
