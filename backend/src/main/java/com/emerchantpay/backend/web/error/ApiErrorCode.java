package com.emerchantpay.backend.web.error;

public enum ApiErrorCode {
	GENERAL_ERROR("Internal server error!"),
	INVALID_INPUT_PARAMETER("%s"),
	BAD_CREDENTIALS("Invalid credentials."),
	NOT_FOUND("Cannot find %s."),
	NON_UNIQUE("%s must be unique."),
	NOT_ALLOWED("Operation not allowed: %s.");

	private final String reasonTemplate;

	ApiErrorCode(String reasonTemplate) {
		this.reasonTemplate = reasonTemplate;
	}

	public String getReasonTemplate() {
		return reasonTemplate;
	}
}
