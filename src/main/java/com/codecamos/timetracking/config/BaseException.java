package com.codecamos.timetracking.config;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

	private final int code;

	public BaseException(final int code, final String message) {
		super(message);
		this.code = code;
	}
}
