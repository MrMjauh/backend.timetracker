package com.codecamos.timetracking.config;

import com.codecamos.timetracking.model.ApiError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	Environment env;

	private static final Logger logger = LogManager.getLogger(RestResponseEntityExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleConflict(
			final Exception ex, final WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ApiError apiError = new ApiError();
		if (ex instanceof BaseException) {
			BaseException baseException = (BaseException) ex;
			apiError.setCode(baseException.getCode());
			apiError.setMessage(baseException.getMessage());
			if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) {
				ResponseStatus responseStatusAnnoation = ex.getClass().getAnnotation(ResponseStatus.class);
				status = responseStatusAnnoation.code();
			}
		}
		else {
			apiError.setCode(Resource.ErrorCode.GENERIC_CODE);
			apiError.setMessage("Something went wrong :(");
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
