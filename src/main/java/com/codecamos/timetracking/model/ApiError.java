package com.codecamos.timetracking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiError {

	private int code;
	private String message;
	private Exception exception;

	public ApiError(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
