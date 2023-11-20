package com.emerchantpay.backend.web.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.emerchantpay.backend.configuration.ConfigurationProperties;
import com.emerchantpay.backend.domain.account.Account;
import com.emerchantpay.backend.domain.account.Admin;
import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.dto.account.AccountDTO;
import com.emerchantpay.backend.dto.account.AdminDTO;
import com.emerchantpay.backend.dto.account.MerchantDTO;
import com.emerchantpay.backend.web.dto.ValueWrapper;
import com.emerchantpay.backend.web.security.AuthenticationService;

@Validated
@RestController
@RequestMapping(value = "auth")
public class LoginController {

	private final AuthenticationService authenticationService;

	public LoginController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping("/login")
	public ResponseEntity<ValueWrapper<String>> login(@RequestParam("username") String username, @RequestParam("password") String password) {
		ConfigurationProperties.LOG.info(String.format("Login %s", username));

		return ResponseEntity.ok(new ValueWrapper<>(authenticationService.authenticate(username, password)));
	}

	@GetMapping("/account")
	public ResponseEntity<AccountDTO> account() {
		Account account = authenticationService.getAuthenticatedAccount();
		AccountDTO result = null;
		if (account instanceof Admin admin) {
			result = new AdminDTO(admin);
		} else if (account instanceof Merchant merchant) {
			result = new MerchantDTO(merchant);
		}
		return ResponseEntity.ok(result);
	}
}
