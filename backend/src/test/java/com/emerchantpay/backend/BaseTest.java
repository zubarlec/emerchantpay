package com.emerchantpay.backend;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.transaction.annotation.Transactional;

import com.emerchantpay.backend.repository.RepositoryRegistry;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootTest
@ComponentScan("com.emerchantpay.backend")
@EnableSpringConfigured
@AutoConfigureTestEntityManager
@TestPropertySources({
	@TestPropertySource("classpath:test.properties")
})
public abstract class BaseTest {

	@PersistenceContext
	protected EntityManager em;

	protected HttpServletRequest request;
	protected HttpServletResponse response;

	@Autowired
	protected RepositoryRegistry repo;

	protected void init() {
		//template method
	}

	@BeforeEach
	@Transactional
	public final void setUp() {

		cleanDB();

		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();

		init();
	}

	private void cleanDB() {
		List<String> result = em.createNativeQuery("SELECT Concat('TRUNCATE TABLE ',TABLE_SCHEMA,'.',TABLE_NAME, ';') FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = 'PUBLIC'").getResultList();

		em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

		result.forEach(truncateStatement -> em.createNativeQuery(truncateStatement).executeUpdate());

		em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
	}
}
