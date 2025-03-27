package com.dongsan.api.support.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @param errors 실패 시 에러 목록 List<ValidationError>
 */
@JsonPropertyOrder({"code", "message", "errors"})
public record ErrorResponse<T>(
	String code,
	String message,
	@JsonInclude(Include.NON_EMPTY)
	T errors
) {
	public ErrorResponse(String code, String message, T errors) {
		this.code = code;
		this.message = message;
		this.errors = errors;
	}

	public static <T> ErrorResponse<T> from(String code, String message) {
		return new ErrorResponse<>(code, message, null);
	}

	public static <T> ErrorResponse<T> from(String code, String message, T errors) {
		return new ErrorResponse<>(code, message, errors);
	}
}
