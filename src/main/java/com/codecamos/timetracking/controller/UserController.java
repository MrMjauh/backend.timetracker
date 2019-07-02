package com.codecamos.timetracking.controller;

import com.codecamos.timetracking.config.BaseException;
import com.codecamos.timetracking.config.Resource;
import com.codecamos.timetracking.config.auth.AccessAuthenticated;
import com.codecamos.timetracking.config.auth.AuthenticatedUser;
import com.codecamos.timetracking.controller.dto.UserDTO;
import com.codecamos.timetracking.model.persistance.User;
import com.codecamos.timetracking.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {

	private IUserService userService;

	@Autowired
	public UserController(IUserService userService) {
		this.userService = userService;
	}

	@AccessAuthenticated
	@GetMapping
	public UserDTO getMe(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		User user = this.userService.getUser(authenticatedUser.getId());

		if (user == null) {
			throw new BaseException(Resource.ErrorCode.RESOURCE_NOT_FOUND, "Can not find user");
		}

		return UserDTO.from(user);
	}
}
