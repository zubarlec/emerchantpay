package com.emerchantpay.backend.dto.account;

import com.emerchantpay.backend.domain.account.Account;
import com.emerchantpay.backend.domain.account.AccountRole;

public abstract class AccountDTO {
	private Long id;
	private String email;
	private AccountRole role;

	protected AccountDTO() {

	}

	protected AccountDTO(Account account) {
		if (account == null) {
			return;
		}
		id = account.getId();
		email = account.getEmail();
		role = account.getRole();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AccountRole getRole() {
		return role;
	}

	public void setRole(AccountRole role) {
		this.role = role;
	}
}
