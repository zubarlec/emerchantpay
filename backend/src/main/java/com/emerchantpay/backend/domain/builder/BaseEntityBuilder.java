package com.emerchantpay.backend.domain.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.emerchantpay.backend.repository.RepositoryRegistry;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;


public abstract class BaseEntityBuilder<T> {
	private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	@Autowired
	protected RepositoryRegistry repo;

	protected T result;
	
	protected abstract JpaRepository<T, Long> getRepository();
	
	protected void afterSave() {
		// template method
	}

	@Transactional
	public T build() {
		validatorFactory.getValidator().validate(result);

		result = getRepository().save(result);

		afterSave();

		return result;
	}
}
