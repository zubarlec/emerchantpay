package com.emerchantpay.backend.domain.transaction;


import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public abstract class AmountTransaction extends Transaction {

	@Column(name = "amount", columnDefinition="DECIMAL(16,3)")
	@NotNull
	@Positive
	private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
