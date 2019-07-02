package com.codecamos.timetracking.controller;

import com.codecamos.timetracking.config.Resource;
import com.codecamos.timetracking.model.ApiError;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WhiteLabelErrorController implements ErrorController {

	private static final String PATH = "/error";

	@RequestMapping(value = PATH)
	public ApiError error() {
		return new ApiError(Resource.ErrorCode.FORBIDDEN_MISSING_OR_MOVE, "The resource you are trying to access is either missing or moved");
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}
}