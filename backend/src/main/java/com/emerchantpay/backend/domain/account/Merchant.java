package com.emerchantpay.backend.domain.account;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import com.emerchantpay.backend.domain.transaction.Transaction;

import jakarta.persistence.*;

@Entity
public class Merchant extends Account {

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private MerchantStatus status;

	@Column(name = "total_transaction_sum", columnDefinition="DECIMAL(16,3)")
	private BigDecimal totalTransactionSum;

	@OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY)
	@OrderBy("timestamp DESC")
	private Set<Transaction> transactions = new LinkedHashSet<>();

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
	public Set<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}
}
