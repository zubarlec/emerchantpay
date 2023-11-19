package com.emerchantpay.backend.web.error;

public class ErrorDTO {
	private String reason;
	private ApiErrorCode code;

	public ErrorDTO() {

	}
	
	public ErrorDTO(String reason, ApiErrorCode code) {
		this.reason = reason;
		this.code = code;
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
}
