package com.emerchantpay.backend.web.jwt.impl;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emerchantpay.backend.web.jwt.JwtAdapter;
import com.emerchantpay.backend.web.jwt.JwtService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtServiceImpl implements JwtService {

	@Value("${emerchantpay.jwt.signingKey:example-of-some-long-enough-signing-key-1234567890}")
	private String jwtSigningKey;

	@Value("${emerchantpay.jwt.accessTokenExpirationMillis:43200000}") // 12 hours = 1000 * 60s * 60m * 12h
	private long accessTokenExpirationMillis;

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		secretKey = Keys.hmacShaKeyFor(jwtSigningKey.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public JwtAdapter getJwtAdapter(String token) {
		return new JwtAdapter(token, secretKey);
	}

	@Override
	public String generateToken(String username) {
		return Jwts.builder()
			.subject(username)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMillis))
			.signWith(secretKey).compact();
	}
}
