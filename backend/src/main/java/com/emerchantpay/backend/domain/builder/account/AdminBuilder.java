package com.emerchantpay.backend.domain.builder.account;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.emerchantpay.backend.domain.account.AccountRole;
import com.emerchantpay.backend.domain.account.Admin;

@Configurable
public class AdminBuilder extends AccountBuilder<Admin> {
	public AdminBuilder(String email) {
		super(email, AccountRole.ROLE_ADMIN);
	}

	@Override
	protected Admin createNew() {
		return new Admin();
	}

	@Override
	protected JpaRepository<Admin, Long> getRepository() {
		return repo.admin;
	}
}
