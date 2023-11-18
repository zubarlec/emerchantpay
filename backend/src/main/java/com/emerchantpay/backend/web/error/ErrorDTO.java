package com.emerchantpay.backend.web.error;

import java.io.Serializable;

public class ErrorDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String reason;
	private ApiErrorCode code;
	private String value;
	
	public ErrorDTO() {

	}
	
	public ErrorDTO(String reason, ApiErrorCode code) {
		this.reason = reason;
		this.code = code;
	}
	
	public ErrorDTO(String reason, ApiErrorCode code, String value) {
		this(reason, code);
		this.value = value;
	}

	public ErrorDTO(ApiErrorCode code, Exception e) {
		this(String.format(code.getReasonTemplate(), e.getMessage()), code);
	}
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public ApiErrorCode getCode() {
		return code;
	}
	public void setCode(ApiErrorCode code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
