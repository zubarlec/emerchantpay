package com.emerchantpay.backend.repository;

import org.springframework.stereotype.Service;

import com.emerchantpay.backend.repository.account.AdminRepository;
import com.emerchantpay.backend.repository.account.MerchantRepository;
import com.emerchantpay.backend.repository.transaction.*;

@Service
public class RepositoryRegistry {
	public final TransactionRepository transaction;
	public final AuthorizeTransactionRepository authorizeTransaction;
	public final ChargeTransactionRepository chargeTransaction;
	public final RefundTransactionRepository refundTransaction;
	public final ReversalTransactionRepository reversalTransaction;
	public final MerchantRepository merchant;
	public final AdminRepository admin;

	public RepositoryRegistry(TransactionRepository transaction, AuthorizeTransactionRepository authorizeTransaction, ChargeTransactionRepository chargeTransaction, RefundTransactionRepository refundTransaction, ReversalTransactionRepository reversalTransaction, MerchantRepository merchant, AdminRepository admin) {
		this.transaction = transaction;
		this.authorizeTransaction = authorizeTransaction;
		this.chargeTransaction = chargeTransaction;
		this.refundTransaction = refundTransaction;
		this.reversalTransaction = reversalTransaction;
		this.merchant = merchant;
		this.admin = admin;
	}
}
