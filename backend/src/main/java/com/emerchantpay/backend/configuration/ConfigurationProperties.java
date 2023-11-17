package com.emerchantpay.backend.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationProperties {

	public static final Logger LOG = LoggerFactory.getLogger("com.emerchantpay.log");

	@Value("${emerchantpay.import.csv.file:}")
	private String importCsvFile;

	public String getImportCsvFile() {
		return importCsvFile;
	}
}
