package com.emerchantpay.backend.web.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtAdapter {

	private final String subject;
	private final Date expirationDate;

	public JwtAdapter(String token, SecretKey secretKey) {
		Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

		subject = claims.getSubject();
		expirationDate = claims.getExpiration();
	}

	public boolean isValid(UserDetails userDetails) {
		boolean isExpired = expirationDate.before(new Date());
		return (subject.equals(userDetails.getUsername())) && !isExpired;
	}

	public String getSubject() {
		return subject;
	}
}
