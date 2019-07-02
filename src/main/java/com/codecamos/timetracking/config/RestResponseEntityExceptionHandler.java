package com.codecamos.timetracking.config;

import com.codecamos.timetracking.model.ApiError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private Environment env;

	private static final Logger logger = LogManager.getLogger(RestResponseEntityExceptionHandler.class);
	public static final HttpStatus DEFAULT_STATUS_CODE = HttpStatus.BAD_REQUEST;

	@Autowired
	public RestResponseEntityExceptionHandler(Environment env) {
		this.env = env;
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleConflict(
			final Exception ex, final WebRequest request) {
		HttpStatus status = DEFAULT_STATUS_CODE;

		ApiError apiError;
		if (ex == null) {
			apiError = Resource.GENERIC_ERROR;
		}
		else if (ex instanceof BaseException) {
			BaseException baseException = (BaseException) ex;
			apiError = new ApiError();
			apiError.setCode(baseException.getCode());
			apiError.setMessage(baseException.getMessage());
			if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) {
				ResponseStatus responseStatusAnnotation = ex.getClass().getAnnotation(ResponseStatus.class);
				status = responseStatusAnnotation.code();
			}
		}
		else if (ex instanceof AccessDeniedException) {
			apiError = Resource.ACCESS_ERROR;
		}
		else {
			apiError = Resource.GENERIC_ERROR;
		}

		logger.error(ex);

		// Make sure exceptions are only sent in dev or stage
		// Depending on your security, maybe even go as far to future toggle this
		if (Resource.isInDevOrStage(this.env.getActiveProfiles())) {
			apiError.setException(ex);
		}

		return handleExceptionInternal(ex, apiError,
				new HttpHeaders(), status, request);
	}
}
