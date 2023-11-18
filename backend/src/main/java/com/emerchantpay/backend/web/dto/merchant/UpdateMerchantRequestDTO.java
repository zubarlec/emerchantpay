package com.emerchantpay.backend.web.dto.merchant;

import com.emerchantpay.backend.dto.account.MerchantDTO;

public class UpdateMerchantRequestDTO {
	private MerchantDTO merchant;
	private String password;

	public MerchantDTO getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantDTO merchant) {
		this.merchant = merchant;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
