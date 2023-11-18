package com.emerchantpay.backend.web.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
		return ResponseEntity.ok(new ValueWrapper<>(authenticationService.authenticate(username, password)));
	}
}
