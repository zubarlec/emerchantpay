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

	/**
	 * Retrieve all the merchants.
	 *
	 * @return a ResponseEntity containing a ListWrapper of MerchantDTO objects
	 */
	@GetMapping("/")
	public ResponseEntity<ListWrapper<MerchantDTO>> getAll() {
		return ResponseEntity.ok(new ListWrapper<>(merchantCrudService.getMerchants()));
	}

	/**
	 * Retrieve a specific merchant by ID.
	 *
	 * @param id the ID of the merchant to retrieve
	 * @return a ResponseEntity containing the MerchantDTO object
	 * @throws EntityNotFoundException if the merchant with the given ID does not exist
	 */
	@GetMapping("/{id}")
	public ResponseEntity<MerchantDTO> getOne(@PathVariable @NotNull Long id) throws EntityNotFoundException {
		return ResponseEntity.ok(merchantCrudService.getMerchant(id));
	}

	/**
	 * Create a new merchant.
	 *
	 * @param merchantRequestDTO the merchant request object containing the merchant details
	 * @return a ResponseEntity containing the MerchantDTO object
	 * @throws DuplicateMailException if a merchant with the same email already exists
	 * @throws InvalidMerchantException if the merchant is invalid or missing required fields
	 */
	@PostMapping("/")
	public ResponseEntity<MerchantDTO> create(@RequestBody @NotNull UpdateMerchantRequestDTO merchantRequestDTO) throws DuplicateMailException, InvalidMerchantException {
		ConfigurationProperties.LOG.info(String.format("Create merchant: %s", JsonUtil.toJson(merchantRequestDTO.getMerchant())));

		return ResponseEntity.ok(merchantCrudService.createMerchant(merchantRequestDTO.getMerchant(), merchantRequestDTO.getPassword()));
	}

	/**
	 * Update an existing merchant.
	 *
	 * @param merchantRequestDTO the merchant request object containing the updated merchant details
	 * @return a ResponseEntity containing the updated MerchantDTO object
	 * @throws DuplicateMailException if a merchant with the same email already exists
	 * @throws InvalidMerchantException if the updated merchant is invalid or missing required fields
	 */
	@PatchMapping("/")
	public ResponseEntity<MerchantDTO> update(@RequestBody @NotNull UpdateMerchantRequestDTO merchantRequestDTO) throws DuplicateMailException, InvalidMerchantException {
		ConfigurationProperties.LOG.info(String.format("Update merchant: %s", JsonUtil.toJson(merchantRequestDTO.getMerchant())));

		return ResponseEntity.ok(merchantCrudService.updateMerchant(merchantRequestDTO.getMerchant(), merchantRequestDTO.getPassword()));
	}

	/**
	 * Delete a merchant by ID.
	 *
	 * @param id the ID of the merchant to be deleted
	 * @return a ResponseEntity containing a ValueWrapper object with a boolean value set to true, indicating a successful deletion
	 * @throws InvalidMerchantException if the merchant is invalid or missing required fields
	 * @throws EntityNotFoundException if the merchant with the specified ID does not exist
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<ValueWrapper<Boolean>> delete(@PathVariable @NotNull Long id) throws InvalidMerchantException, EntityNotFoundException {
		ConfigurationProperties.LOG.info(String.format("Delete merchant: %d", id));

		merchantCrudService.deleteMerchant(id);
		return ResponseEntity.ok(new ValueWrapper<>(true));
	}
}
