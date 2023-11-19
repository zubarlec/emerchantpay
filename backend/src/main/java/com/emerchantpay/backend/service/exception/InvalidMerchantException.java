package com.emerchantpay.backend.service.exception;

public class InvalidMerchantException extends Exception {
	public InvalidMerchantException() {
		super();
	}

	public InvalidMerchantException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMerchantException(Throwable cause) {
		super(cause);
	}
}
