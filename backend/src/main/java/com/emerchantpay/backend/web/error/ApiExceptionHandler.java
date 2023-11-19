package com.emerchantpay.backend.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.emerchantpay.backend.service.exception.DuplicateMailException;
import com.emerchantpay.backend.service.exception.EntityNotFoundException;
import com.emerchantpay.backend.service.exception.InvalidMerchantException;
import com.emerchantpay.backend.service.exception.InvalidTransactionException;

@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorDTO handleNoHandlerFoundException(Exception e) {
		return new ErrorDTO(ApiErrorCode.GENERAL_ERROR, e);
	}

	@ExceptionHandler(DuplicateMailException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO handleError(DuplicateMailException exception) {
		return new ErrorDTO(ApiErrorCode.NON_UNIQUE, exception);
	}

	@ExceptionHandler(InvalidMerchantException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO handleError(InvalidMerchantException exception) {
		return new ErrorDTO(ApiErrorCode.INVALID_INPUT_PARAMETER, exception);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO handleError(EntityNotFoundException exception) {
		return new ErrorDTO(ApiErrorCode.NOT_FOUND, exception);
	}

	@ExceptionHandler(InvalidTransactionException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO handleError(InvalidTransactionException exception) {
		return new ErrorDTO(ApiErrorCode.INVALID_INPUT_PARAMETER, exception);
	}

	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO handleError(AuthenticationException exception) {
		return new ErrorDTO(ApiErrorCode.BAD_CREDENTIALS, exception);
	}

}
