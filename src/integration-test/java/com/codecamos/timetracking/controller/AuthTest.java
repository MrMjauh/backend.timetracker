package com.codecamos.timetracking.controller;

import com.codecamos.timetracking.config.Resource;
import com.codecamos.timetracking.config.auth.SecurityResource;
import com.codecamos.timetracking.config.auth.TokenAuthenticationFilter;
import com.codecamos.timetracking.controller.dto.UserDTO;
import com.codecamos.timetracking.model.persistance.User;
import com.codecamos.timetracking.service.ITokenService;
import com.codecamos.timetracking.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("prod")
public class AuthTest {

	@MockBean
	private IUserService userService;

	@MockBean
	private ITokenService tokenService;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mvc;

	@BeforeEach
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(new TokenAuthenticationFilter(tokenService)).build();
	}

	@AfterEach
	public void cleanUp() {
		reset(userService, tokenService);
	}

	@Test
	public void getMe_ValidUser_ShouldReturnUser() throws Exception {
		when(tokenService.getUserFromToken(any())).thenReturn(dummyUser());
		when(userService.getUser(any())).thenReturn(dummyUser());

		mvc.perform(get("/user").header(SecurityResource.X_AUTH_KEY, "dummy_token"))
				.andExpect(content().string(dummyUserAsString()))
				.andExpect(status().isOk());
	}

	@Test
	public void getMe_NoValidUser_ShouldGiveBadRequestFromFilter() throws Exception {
		when(tokenService.getUserFromToken(any())).thenReturn(null);

		mvc.perform(get("/user").header(SecurityResource.X_AUTH_KEY, "dummy_token"))
				.andExpect(content().string(genericErrorAsString()))
				.andExpect(status().isBadRequest());
	}

	private User dummyUser() {
		User user = new User();
		user.setEmail("dummy@dummy.se");
		user.setId(new ObjectId());
		HashSet<String> roles = new HashSet<>();
		roles.add(SecurityResource.Role.USER);
		user.setRoles(roles);
		return user;
	}

	private String dummyUserAsString() throws JsonProcessingException {
		return mapper.writeValueAsString(UserDTO.from(dummyUser()));
	}

	private String genericErrorAsString() throws JsonProcessingException {
		return mapper.writeValueAsString(Resource.GENERIC_ERROR);
	}
}
