package com.emerchantpay.backend.web.configuration;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.emerchantpay.backend.domain.account.Account;
import com.emerchantpay.backend.repository.RepositoryRegistry;
import com.emerchantpay.backend.util.JsonUtil;
import com.emerchantpay.backend.web.error.ApiErrorCode;
import com.emerchantpay.backend.web.error.ErrorDTO;
import com.emerchantpay.backend.web.jwt.JwtAuthenticationFilter;
import com.emerchantpay.backend.web.jwt.JwtService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

	private final PasswordEncoder passwordEncoder;
	private final RepositoryRegistry repo;
	private final JwtService jwtService;

	public SecurityConfiguration(PasswordEncoder passwordEncoder, RepositoryRegistry repo, JwtService jwtService) {
		this.passwordEncoder = passwordEncoder;
		this.repo = repo;
		this.jwtService = jwtService;
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(userDetails());
		provider.setForcePrincipalAsString(true);
		return provider;
	}

	@Bean
	UserDetailsService userDetails() {
		return username -> {
			if (username == null || username.isEmpty()) {
				throw new UsernameNotFoundException("Empty email.");
			}
			Account account = repo.account.findByEmailAllIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException(String.format("Invalid email: %s", username)));

			return new User(account.getEmail(), account.getPassword(), List.of(new SimpleGrantedAuthority(account.getRole().name())));
		};
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.anonymous(AbstractHttpConfigurer::disable)
			.exceptionHandling(configurer -> configurer
				.accessDeniedHandler(accessDeniedHandler())
				.authenticationEntryPoint(authenticationEntryPoint()))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(antMatcher("/api/v1/auth/login/**")).permitAll()

				.requestMatchers(antMatcher("/api/v1/merchants/**")).hasRole("ADMIN")
				.requestMatchers(antMatcher("/api/v1/transaction/**")).hasAnyRole("ADMIN", "MERCHANT")

				.anyRequest().hasRole("ADMIN"))
			.csrf(AbstractHttpConfigurer::disable) // disable CSRF, because not required for task
			.build();
	}

	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtService, userDetails());
	}

	@Bean
	AccessDeniedHandler accessDeniedHandler() {
		return (request, response, accessDeniedException) -> {
			if (response.isCommitted()) {
				return;
			}
			ErrorDTO object = new ErrorDTO(String.format(ApiErrorCode.NOT_ALLOWED.getReasonTemplate(), "Forbidden"), ApiErrorCode.NOT_ALLOWED);

			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().print(JsonUtil.toJson(object));
		};
	}

	@Bean
	AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, authException) -> {
			ErrorDTO object = new ErrorDTO(ApiErrorCode.BAD_CREDENTIALS.getReasonTemplate(), ApiErrorCode.BAD_CREDENTIALS);

			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().print(JsonUtil.toJson(object));
		};
	}
}
