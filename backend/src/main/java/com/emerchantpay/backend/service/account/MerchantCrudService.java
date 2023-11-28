package com.emerchantpay.backend.service.account;

import java.util.List;

import com.emerchantpay.backend.dto.account.MerchantDTO;
import com.emerchantpay.backend.service.exception.DuplicateMailException;
import com.emerchantpay.backend.service.exception.EntityNotFoundException;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;

public interface MerchantCrudService {

	MerchantDTO createOrUpdateMerchant(MerchantDTO merchantDTO, String password) throws InvalidMerchantException, DuplicateMailException;

	List<MerchantDTO> getMerchants();

	void deleteMerchant(Long id) throws InvalidMerchantException, EntityNotFoundException;

	MerchantDTO getMerchant(Long id) throws EntityNotFoundException;
}
