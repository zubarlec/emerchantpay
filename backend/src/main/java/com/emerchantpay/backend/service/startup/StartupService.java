package com.emerchantpay.backend.service.startup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.emerchantpay.backend.configuration.ConfigurationProperties;
import com.emerchantpay.backend.repository.RepositoryRegistry;
import com.emerchantpay.backend.service.account.ImportAccountsService;
import com.emerchantpay.backend.service.exception.ImportException;

@Service
@Profile("main")
public class StartupService implements CommandLineRunner {

	private final RepositoryRegistry repo;
	private final ImportAccountsService importAccountsService;

	private final ConfigurationProperties configuration;

	public StartupService(RepositoryRegistry repo, ImportAccountsService importAccountsService, ConfigurationProperties configuration) {
		this.repo = repo;
		this.importAccountsService = importAccountsService;
		this.configuration = configuration;
	}

	@Override
	public void run(String... args) {
		importFromCsvIfNeeded();
	}

	void importFromCsvIfNeeded() {
		long accountsCount = repo.account.count();
		if (accountsCount > 0) {
			ConfigurationProperties.LOG.info(String.format("Starting with account count = %d", accountsCount));
			return;
		}
		if (configuration.getImportCsvFile().isEmpty()) {
			return;
		}

		try {
			ConfigurationProperties.LOG.info(String.format("Try to import accounts from %s", configuration.getImportCsvFile()));

			importAccountsService.importFromCsv(Files.newInputStream(Path.of(configuration.getImportCsvFile())));
		} catch (ImportException | IOException e) {
			ConfigurationProperties.LOG.error(String.format("Failed to import from CSV: %s", e.getMessage()));
		}
	}
}
