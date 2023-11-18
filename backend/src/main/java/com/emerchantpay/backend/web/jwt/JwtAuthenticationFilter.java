package com.emerchantpay.backend.web.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		if (!StringUtils.hasLength(authHeader) || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		JwtAdapter jwtAdapter = jwtService.getJwtAdapter(authHeader.substring(7));

		if (StringUtils.hasLength(jwtAdapter.getSubject()) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAdapter.getSubject());
			if (jwtAdapter.isValid(userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(userDetails);

				SecurityContext context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(authToken);
				SecurityContextHolder.setContext(context);
			}
		}
		filterChain.doFilter(request, response);
	}
}
