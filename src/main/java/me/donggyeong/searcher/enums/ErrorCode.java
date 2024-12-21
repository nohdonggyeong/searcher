package me.donggyeong.searcher.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	INVALID_TARGET(HttpStatus.BAD_REQUEST.value(), "The value of target is invalid"),
	OPENSEARCH_SEARCH_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A failure occurred in the opensearch search operation."),
	OPENSEARCH_AGG_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A failure occurred in the opensearch aggregate operation.");

	private final int statusCode;
	private final String message;

	ErrorCode(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}
}
