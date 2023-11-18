package com.emerchantpay.backend.util;

import com.emerchantpay.backend.configuration.ConfigurationProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static ObjectMapper mapper;
	private static ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper()
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.enable(DeserializationFeature.USE_LONG_FOR_INTS)
				.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		}
		return mapper;
	}

	public static String toJson(Object o) {
		if (o == null) {
			return null;
		}

		try {
			return getMapper().writeValueAsString(o);
		} catch (Throwable e) {
			ConfigurationProperties.LOG.error("Error parsing to JSON", e);
			return null;
		}
	}

	public static <T> T readObject(String json, Class<T> clazz) {
		if (json == null || json.isEmpty()) {
			return null;
		}

		T result = null;
		try {
			result = getMapper().readValue(json, clazz);
		} catch (Throwable e) {
			ConfigurationProperties.LOG.error(String.format("Error parsing from JSON string: %s", json), e);
		}
		return result;
	}

}
