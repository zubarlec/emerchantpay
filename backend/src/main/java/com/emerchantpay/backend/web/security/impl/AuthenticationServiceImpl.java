package com.emerchantpay.backend.web.security.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.emerchantpay.backend.configuration.ConfigurationProperties;
import com.emerchantpay.backend.domain.account.Account;
import com.emerchantpay.backend.repository.RepositoryRegistry;
import com.emerchantpay.backend.web.jwt.JwtService;
import com.emerchantpay.backend.web.security.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final AuthenticationManager authenticationManager;
	private final RepositoryRegistry repo;
	private final JwtService jwtService;

	public AuthenticationServiceImpl(AuthenticationManager authenticationManager, RepositoryRegistry repo, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.repo = repo;
		this.jwtService = jwtService;
	}

	@Override
	public Account getAuthenticatedAccount() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}

		Object details = authentication.getDetails();
		Account result = null;
		if (details instanceof UserDetails userDetails) {
			result = repo.account.findByEmailAllIgnoreCase(userDetails.getUsername()).orElse(null);
		} else {
			ConfigurationProperties.LOG.error(String.format("Authentication details is of unknown type. Failed to obtain authenticated account: %s", details));
		}
		return result;
	}

	@Override
	public String authenticate(String username, String password) {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

		return jwtService.generateToken((String) authentication.getPrincipal());
	}
}
