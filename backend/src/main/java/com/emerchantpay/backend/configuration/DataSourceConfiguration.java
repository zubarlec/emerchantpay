package com.emerchantpay.backend.configuration;

import static java.util.Map.entry;

import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableJpaRepositories(basePackages = "com.emerchantpay.backend.repository")
public class DataSourceConfiguration {

	private final DataSource dataSource;

	public DataSourceConfiguration(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
		result.setDataSource(dataSource);
		result.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		result.setJpaPropertyMap(Map.ofEntries(
			entry("hibernate.enable_lazy_load_no_trans", true),
			entry("hibernate.hbm2ddl.auto", "update")
		));
		result.setPackagesToScan("com.emerchantpay.backend.domain");

		return result;
	}
}
