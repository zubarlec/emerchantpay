package com.emerchantpay.backend.domain.transaction;

import com.emerchantpay.backend.domain.account.Merchant;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Transaction {

	@Transient
	public abstract TransactionType getType();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "uuid", unique = true, nullable = false)
	@NotEmpty
	private String uuid;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	@NotNull
	private TransactionStatus status;

	@Column(name = "timestamp", nullable = false)
	@NotNull
	private long timestamp;

	@Column(name = "customer_email", nullable = false)
	@NotEmpty
	@Email
	private String customerEmail;

	@Column(name = "customer_phone")
	private String customerPhone;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reference_id")
	private Transaction referenceTransaction;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "merchant")
	@NotNull
	private Merchant merchant;

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
	public Transaction getReferenceTransaction() {
		return referenceTransaction;
	}
	public void setReferenceTransaction(Transaction referenceTransaction) {
		this.referenceTransaction = referenceTransaction;
	}
	public Merchant getMerchant() {
		return merchant;
	}
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
}
