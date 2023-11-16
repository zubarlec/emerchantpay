package com.emerchantpay.backend.dto.transaction;

import java.math.BigDecimal;

import com.emerchantpay.backend.domain.transaction.AmountTransaction;
import com.emerchantpay.backend.domain.transaction.Transaction;
import com.emerchantpay.backend.domain.transaction.TransactionStatus;
import com.emerchantpay.backend.domain.transaction.TransactionType;

public class TransactionDTO {
	private Long id;
	private String uuid;
	private TransactionStatus status;
	private long timestamp;
	private String customerEmail;
	private String customerPhone;
	private Long referenceId;
	private BigDecimal amount;
	private TransactionType type;

	public TransactionDTO() {

	}

	public TransactionDTO(Transaction transaction) {
		if (transaction == null) {
			return;
		}
		id = transaction.getId();
		uuid = transaction.getUuid();
		status = transaction.getStatus();
		timestamp = transaction.getTimestamp();
		customerEmail = transaction.getCustomerEmail();
		customerPhone = transaction.getCustomerPhone();
		referenceId = transaction.getReferenceTransaction() == null ? null : transaction.getReferenceTransaction().getId();
		type = TransactionType.getType(transaction);

		if (transaction instanceof AmountTransaction) {
			amount = ((AmountTransaction) transaction).getAmount();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}
}
