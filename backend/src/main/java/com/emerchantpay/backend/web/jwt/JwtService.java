package com.emerchantpay.backend.web.jwt;

public interface JwtService {
	JwtAdapter getJwtAdapter(String token);

	String generateToken(String username);
}
