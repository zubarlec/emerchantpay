package com.emerchantpay.backend.service.account;

import java.io.InputStream;

import com.emerchantpay.backend.service.exception.ImportException;

public interface ImportAccountsService {
	void importFromCsv(InputStream is) throws ImportException;
}
