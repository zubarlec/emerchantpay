package com.emerchantpay.backend.domain.builder.account;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.emerchantpay.backend.domain.account.AccountRole;
import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.account.MerchantStatus;

@Configurable
public class MerchantBuilder extends AccountBuilder<Merchant> {
	public MerchantBuilder(String email) {
		super(email, AccountRole.ROLE_MERCHANT);
		result.setStatus(MerchantStatus.MERCHANT_ACTIVE);
	}
	public MerchantBuilder(Merchant other) {
		super(other);
	}

	@Override
	protected Merchant createNew() {
		return new Merchant();
	}

	@Override
	protected JpaRepository<Merchant, Long> getRepository() {
		return repo.merchant;
	}

	public MerchantBuilder withName(String name) {
		result.setName(name);
		return this;
	}
	public MerchantBuilder withDescription(String description) {
		result.setDescription(description);
		return this;
	}
	public MerchantBuilder withStatus(MerchantStatus status) {
		result.setStatus(status);
		return this;
	}
	public MerchantBuilder withTotalTransactionSum(BigDecimal totalTransactionSum) {
		result.setTotalTransactionSum(totalTransactionSum);
		return this;
	}
}
