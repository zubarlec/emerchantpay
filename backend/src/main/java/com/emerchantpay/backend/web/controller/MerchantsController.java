package com.emerchantpay.backend.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.emerchantpay.backend.dto.account.MerchantDTO;
import com.emerchantpay.backend.service.account.MerchantCrudService;
import com.emerchantpay.backend.service.exception.DuplicateMailException;
import com.emerchantpay.backend.service.exception.EntityNotFoundException;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;
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
	public ResponseEntity<ListWrapper<MerchantDTO>> getMerchants() {
		return ResponseEntity.ok(new ListWrapper<>(merchantCrudService.getMerchants()));
	}

	@PostMapping("/")
	public ResponseEntity<MerchantDTO> createOrUpdateMerchant(@RequestBody @NotNull UpdateMerchantRequestDTO merchantRequestDTO) throws DuplicateMailException, InvalidMerchantException {
		return ResponseEntity.ok(merchantCrudService.createOrUpdateMerchant(merchantRequestDTO.getMerchant(), merchantRequestDTO.getPassword()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ValueWrapper<Boolean>> deleteMerchant(@PathVariable @NotNull Long id) throws InvalidMerchantException, EntityNotFoundException {
		merchantCrudService.deleteMerchant(id);
		return ResponseEntity.ok(new ValueWrapper<>(true));
	}
}
