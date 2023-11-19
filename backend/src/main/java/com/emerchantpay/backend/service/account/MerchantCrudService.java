package com.emerchantpay.backend.service.account;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emerchantpay.backend.domain.account.Merchant;
import com.emerchantpay.backend.domain.builder.account.MerchantBuilder;
import com.emerchantpay.backend.dto.account.MerchantDTO;
import com.emerchantpay.backend.repository.RepositoryRegistry;
import com.emerchantpay.backend.service.exception.DuplicateMailException;
import com.emerchantpay.backend.service.exception.EntityNotFoundException;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;

import jakarta.validation.ConstraintViolationException;

@Service
public class MerchantCrudService {

	private final RepositoryRegistry repo;
	private final PasswordEncoder passwordEncoder;

	public MerchantCrudService(RepositoryRegistry repo, PasswordEncoder passwordEncoder) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(rollbackFor = {InvalidMerchantException.class, DuplicateMailException.class})
	public MerchantDTO createOrUpdateMerchant(MerchantDTO merchantDTO, String password) throws InvalidMerchantException, DuplicateMailException {

		MerchantBuilder builder;
		if (merchantDTO.getId() != null) {
			Merchant existing = repo.merchant.findById(merchantDTO.getId()).orElse(null);
			builder = new MerchantBuilder(existing);
		} else {
			builder = new MerchantBuilder(merchantDTO.getEmail());
		}

		if (merchantDTO.getEmail() != null) {
			builder.withEmail(merchantDTO.getEmail());
		}
		if (merchantDTO.getName() != null) {
			builder.withName(merchantDTO.getName());
		}
		if (merchantDTO.getDescription() != null) {
			builder.withDescription(merchantDTO.getDescription());
		}
		if (merchantDTO.getStatus() != null) {
			builder.withStatus(merchantDTO.getStatus());
		}
		if (password != null) {
			builder.withPassword(passwordEncoder.encode(password));
		}

		try {
			return new MerchantDTO(builder.build());
		} catch (ConstraintViolationException e) {
			throw new InvalidMerchantException(e);
		} catch (JpaSystemException e) {
			throw new DuplicateMailException();
		}
	}

	public List<MerchantDTO> getMerchants() {
		return repo.merchant.findAll(Sort.by("id")).stream().map(MerchantDTO::new).toList();
	}

	public void deleteMerchant(Long id) throws InvalidMerchantException, EntityNotFoundException {
		if (id == null) {
			throw new EntityNotFoundException("null merchant id");
		}
		try {
			repo.merchant.deleteById(id);
		} catch (Throwable e) {
			throw new InvalidMerchantException("Not allowed to delete merchant with transactions", e);
		}
	}

	public MerchantDTO getMerchant(Long id) throws EntityNotFoundException {
		if (id == null) {
			throw new EntityNotFoundException("null merchant id");
		}

		return new MerchantDTO(repo.merchant.findById(id).orElseThrow(() -> new EntityNotFoundException("invalid id")));
	}
}
