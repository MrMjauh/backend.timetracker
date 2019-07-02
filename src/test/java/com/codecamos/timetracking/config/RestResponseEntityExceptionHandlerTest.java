package com.codecamos.timetracking.config;

import com.codecamos.timetracking.model.ApiError;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RestResponseEntityExceptionHandlerTest {

	@Test
	public void handleConflict_nullException_shouldOutputGenericError() {
		RestResponseEntityExceptionHandler handle =	new RestResponseEntityExceptionHandler(devEnv());
		ResponseEntity<Object> object = handle.handleConflict(null, null);
		ApiError error = (ApiError) object.getBody();
		assertEquals(Resource.GENERIC_ERROR, error);
	}

	@Test
	public void handleConflict_baseException_shouldOutputCustomApiError() {
		String MESSAGE  = "Hello";
		int CODE = 0;
		RestResponseEntityExceptionHandler handle =	new RestResponseEntityExceptionHandler(devEnv());
		BaseException baseException = new BaseException(CODE, MESSAGE);
		ResponseEntity<Object> object = handle.handleConflict(baseException, null);

		ApiError error = (ApiError) object.getBody();
		assertEquals(CODE, error.getCode());
		assertEquals(MESSAGE, error.getMessage());
	}

	@Test
	public void handleConflict_nullPointer_shouldOutputGenericApiError() {
		RestResponseEntityExceptionHandler handle =	new RestResponseEntityExceptionHandler(devEnv());
		ResponseEntity<Object> object = handle.handleConflict(new NullPointerException(), null);

		ApiError error = (ApiError) object.getBody();
		assertEquals(Resource.GENERIC_ERROR, error);
	}

	@Test
	public void handleConflict_devProfile_shouldSetStackTrace() {
		RestResponseEntityExceptionHandler handle =	new RestResponseEntityExceptionHandler(devEnv());
		ResponseEntity<Object> object = handle.handleConflict(new NullPointerException(), null);

		ApiError error = (ApiError) object.getBody();
		assertNotNull(error.getException());
	}

	@Test
	public void handleConflict_stageProfile_shouldSetStackTrace() {
		RestResponseEntityExceptionHandler handle =	new RestResponseEntityExceptionHandler(stageEnv());
		ResponseEntity<Object> object = handle.handleConflict(new NullPointerException(), null);

		ApiError error = (ApiError) object.getBody();
		assertNotNull(error.getException());
	}

	@Test
	public void handleConflict_prodProfile_shouldNotHaveStackTrace() {
		RestResponseEntityExceptionHandler handle =	new RestResponseEntityExceptionHandler(prodEnv());
		ResponseEntity<Object> object = handle.handleConflict(new NullPointerException(), null);

		ApiError error = (ApiError) object.getBody();
		assertNull(error.getException());
	}

	@Test
	public void handleConflict_baseExceptionResponseStatus_shouldChangeStatusCode() {
		RestResponseEntityExceptionHandler handle =	new RestResponseEntityExceptionHandler(prodEnv());
		BaseExceptionTeapot exWithStatusCodeTeapot = new BaseExceptionTeapot(0, "");

		ResponseEntity<Object> object = handle.handleConflict(exWithStatusCodeTeapot, null);
		assertEquals(HttpStatus.I_AM_A_TEAPOT, object.getStatusCode());
	}

	@Test
	public void handleConflict_baseException_shouldReturnDefaultStatusCode() {
		RestResponseEntityExceptionHandler handle =	new RestResponseEntityExceptionHandler(prodEnv());
		BaseException ex = new BaseException(0, "");

		ResponseEntity<Object> object = handle.handleConflict(ex, null);
		assertEquals(RestResponseEntityExceptionHandler.DEFAULT_STATUS_CODE, object.getStatusCode());
	}

	@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT)
	private class BaseExceptionTeapot extends BaseException {

		public BaseExceptionTeapot(int code, String message) {
			super(code, message);
		}
	}

	private Environment devEnv() {
		return getEnvWithProfile(Resource.Profile.DEVELOPMENT);
	}

	private Environment prodEnv() {
		return getEnvWithProfile(Resource.Profile.PRODUCTION);
	}

	private Environment stageEnv() {
		return getEnvWithProfile(Resource.Profile.STAGE);
	}

	private Environment getEnvWithProfile(String profile) {
		Environment devEnv = mock(Environment.class);
		when(devEnv.getActiveProfiles()).thenReturn(new String[]{profile});
		return devEnv;
	}

	private ResponseStatus getResponseStatus(HttpStatus status) {
		ResponseStatus resp = mock(ResponseStatus.class);
		when(resp.code()).thenReturn(status);
		return resp;
	}
}
