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

	public ApiError(long code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		else if (o == this) {
			return true;
		} else if (!o.getClass().equals(this.getClass())) {
			return false;
		}

		ApiError other = (ApiError) o;
		return 	other.message.equals(this.message) &&
				other.code == this.code &&
				other.exception.equals(this.exception);
	}
}
