package com.emerchantpay.backend.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.emerchantpay.backend.configuration.ConfigurationProperties;
import com.emerchantpay.backend.dto.account.MerchantDTO;
import com.emerchantpay.backend.service.account.MerchantCrudService;
import com.emerchantpay.backend.service.exception.DuplicateMailException;
import com.emerchantpay.backend.service.exception.EntityNotFoundException;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;
import com.emerchantpay.backend.util.JsonUtil;
import com.emerchantpay.backend.web.dto.ListWrapper;
import com.emerchantpay.backend.web.dto.ValueWrapper;
import com.emerchantpay.backend.web.dto.merchant.UpdateMerchantRequestDTO;

import jakarta.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping(value = "merchants")
public class MerchantsController {

	private final MerchantCrudService merchantCrudService;

	public MerchantsController(MerchantCrudService merchantCrudService) {
		this.merchantCrudService = merchantCrudService;
	}

	@GetMapping("/")
	public ResponseEntity<ListWrapper<MerchantDTO>> getAll() {
		return ResponseEntity.ok(new ListWrapper<>(merchantCrudService.getMerchants()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<MerchantDTO> getOne(@PathVariable @NotNull Long id) throws EntityNotFoundException {
		return ResponseEntity.ok(merchantCrudService.getMerchant(id));
	}

	@PostMapping("/")
	public ResponseEntity<MerchantDTO> create(@RequestBody @NotNull UpdateMerchantRequestDTO merchantRequestDTO) throws DuplicateMailException, InvalidMerchantException {
		ConfigurationProperties.LOG.info(String.format("Create merchant: %s", JsonUtil.toJson(merchantRequestDTO.getMerchant())));

		return ResponseEntity.ok(merchantCrudService.createMerchant(merchantRequestDTO.getMerchant(), merchantRequestDTO.getPassword()));
	}

	@PatchMapping("/")
	public ResponseEntity<MerchantDTO> update(@RequestBody @NotNull UpdateMerchantRequestDTO merchantRequestDTO) throws DuplicateMailException, InvalidMerchantException {
		ConfigurationProperties.LOG.info(String.format("Update merchant: %s", JsonUtil.toJson(merchantRequestDTO.getMerchant())));

		return ResponseEntity.ok(merchantCrudService.updateMerchant(merchantRequestDTO.getMerchant(), merchantRequestDTO.getPassword()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ValueWrapper<Boolean>> delete(@PathVariable @NotNull Long id) throws InvalidMerchantException, EntityNotFoundException {
		ConfigurationProperties.LOG.info(String.format("Delete merchant: %d", id));

		merchantCrudService.deleteMerchant(id);
		return ResponseEntity.ok(new ValueWrapper<>(true));
	}
}
