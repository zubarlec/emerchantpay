package com.emerchantpay.backend.domain.builder.account;

import com.emerchantpay.backend.domain.account.Account;
import com.emerchantpay.backend.domain.account.AccountRole;
import com.emerchantpay.backend.domain.builder.BaseEntityBuilder;

public abstract class AccountBuilder<T extends Account> extends BaseEntityBuilder<T> {
	protected abstract T createNew();

	protected AccountBuilder(String email, AccountRole role) {
		result = createNew();
		result.setEmail(email);
		result.setRole(role);
	}
	protected AccountBuilder(T other) {
		result = other;
	}

	public AccountBuilder<T> withEmail(String email) {
		String emailActual = email != null ? email.toLowerCase() : null;
		result.setEmail(emailActual);
		return this;
	}

	public AccountBuilder<T> withPassword(String password) {
		result.setPassword(password);
		return this;
	}
}
