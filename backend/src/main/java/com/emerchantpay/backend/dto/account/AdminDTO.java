package com.emerchantpay.backend.dto.account;

import com.emerchantpay.backend.domain.account.Admin;

public class AdminDTO extends AccountDTO {
	public AdminDTO() {
		super();
	}

	public AdminDTO(Admin admin) {
		super(admin);
	}
}
