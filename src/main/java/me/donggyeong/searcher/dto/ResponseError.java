package me.donggyeong.searcher.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResponseError {
	private int statusCode;
	private String errorCode;
	private String message;
}
