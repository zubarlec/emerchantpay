package com.emerchantpay.backend.service.account;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emerchantpay.backend.configuration.ConfigurationProperties;
import com.emerchantpay.backend.domain.account.Account;
import com.emerchantpay.backend.domain.account.MerchantStatus;
import com.emerchantpay.backend.domain.builder.account.AdminBuilder;
import com.emerchantpay.backend.domain.builder.account.MerchantBuilder;
import com.emerchantpay.backend.service.exception.ImportException;

@Service
public class ImportAccountsService {
	private final PasswordEncoder passwordEncoder;

	public ImportAccountsService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(rollbackFor = Throwable.class)
	public void importFromCsv(InputStream is) throws ImportException {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				String type = csvRecord.get("type");
				String email = csvRecord.get("email");
				String password = passwordEncoder.encode(csvRecord.get("password"));

				Account result = switch (type) {
				case "admin" -> new AdminBuilder(email).withPassword(password).build();
				case "merchant" ->
					new MerchantBuilder(email)
						.withName(csvRecord.get("name"))
						.withDescription(csvRecord.get("description"))
						.withStatus(MerchantStatus.valueOf(csvRecord.get("status")))
						.withPassword(password)
						.build();
					default -> null;
				};

				if (result != null) {
					ConfigurationProperties.LOG.info(String.format("Imported account: %s", email));
				} else {
					ConfigurationProperties.LOG.error(String.format("Unexpected type: %s for account %s. Skipping.", type, email));
				}
			}
		} catch (Throwable e) {
			throw new ImportException(e);
		}
	}
}
