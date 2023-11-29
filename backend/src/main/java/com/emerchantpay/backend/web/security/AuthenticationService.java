package com.emerchantpay.backend.web.security;

import com.emerchantpay.backend.domain.account.Account;

public interface AuthenticationService {
	Account getAuthenticatedAccount();

	String authenticate(String username, String password);
}
