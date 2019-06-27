package com.codecamos.timetracking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiError {

	private long code;
	private String message;
	private Exception exception;

}
