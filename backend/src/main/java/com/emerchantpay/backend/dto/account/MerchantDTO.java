package com.emerchantpay.backend.dto.account;

import java.math.BigDecimal;
import java.util.Objects;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.account.MerchantStatus;

public class MerchantDTO extends AccountDTO {
	private String name;
	private String description;
	private MerchantStatus status;
	private BigDecimal totalTransactionSum;

	public MerchantDTO() {
		super();
	}

	public MerchantDTO(Merchant merchant) {
		super(merchant);
		if (merchant == null) {
			return;
		}
		name = merchant.getName();
		description = merchant.getDescription();
		status = merchant.getStatus();
		totalTransactionSum = merchant.getTotalTransactionSum();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MerchantDTO that = (MerchantDTO) o;
		return Objects.equals(name, that.name)
			&& Objects.equals(description, that.description)
			&& status == that.status
			&& Objects.equals(totalTransactionSum, that.totalTransactionSum);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, status, totalTransactionSum);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MerchantStatus getStatus() {
		return status;
	}

	public void setStatus(MerchantStatus status) {
		this.status = status;
	}

	public BigDecimal getTotalTransactionSum() {
		return totalTransactionSum;
	}

	public void setTotalTransactionSum(BigDecimal totalTransactionSum) {
		this.totalTransactionSum = totalTransactionSum;
	}
}
