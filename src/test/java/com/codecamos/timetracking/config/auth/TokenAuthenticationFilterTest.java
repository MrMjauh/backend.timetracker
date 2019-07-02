package com.codecamos.timetracking.config.auth;

import com.codecamos.timetracking.model.persistance.User;
import com.codecamos.timetracking.service.ITokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenAuthenticationFilterTest {

	@BeforeEach
	public void reset() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@Test
	public void doFilterInternal_requestAuthorizationHeaderNull_shouldNotPopulateContext() throws ServletException, IOException {
		String TOKEN = null;
		TokenAuthenticationFilter filter = new TokenAuthenticationFilter(mockedTokenService(null));

		HttpServletRequest request = getRequestWithAuthorizationHeader(TOKEN);
		filter.doFilterInternal(request, null, nopFilterChain());
		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

	@Test
	public void doFilterInternal_tokenServiceNoFoundUser_shouldNotPopulateContext() throws ServletException, IOException {
		String TOKEN = "dummy_token";
		TokenAuthenticationFilter filter = new TokenAuthenticationFilter(mockedTokenService(null));

		HttpServletRequest request = getRequestWithAuthorizationHeader(TOKEN);
		filter.doFilterInternal(request, null, nopFilterChain());
		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

	@Test
	public void doFilterInternal_shouldPopulateContext() throws ServletException, IOException {
		String TOKEN = "dummy_token";
		User user = new User();
		user.setEmail("dummy@dummy.se");
		TokenAuthenticationFilter filter = new TokenAuthenticationFilter(mockedTokenService(user));

		HttpServletRequest request = getRequestWithAuthorizationHeader(TOKEN);
		filter.doFilterInternal(request, null, nopFilterChain());
		assertNotNull(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		assertEquals(AuthenticatedUser.from(user), SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}

	@Test
	public void doFilterInternal_shouldPopulateCorrectRoles() throws ServletException, IOException {
		String TOKEN = "dummy_token";
		User user = new User();
		user.setEmail("dummy@dummy.se");
		HashSet<String> roles = new HashSet<>();
		roles.add("ADMIN");
		roles.add("USER");
		user.setRoles(roles);
		TokenAuthenticationFilter filter = new TokenAuthenticationFilter(mockedTokenService(user));

		HttpServletRequest request = getRequestWithAuthorizationHeader(TOKEN);
		filter.doFilterInternal(request, null, nopFilterChain());
		assertEquals(roles.size(), ((AuthenticatedUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRoles().size());
		assertEquals(AuthenticatedUser.from(user).getRoles(), ((AuthenticatedUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRoles());
	}

	private ITokenService mockedTokenService(User userToReturnOnTokenQuery) {
		ITokenService service = mock(ITokenService.class);
		when(service.getUserFromToken(anyString())).thenReturn(userToReturnOnTokenQuery);

		return service;
	}

	private HttpServletRequest getRequestWithAuthorizationHeader(String data) {
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		when(mockRequest.getHeader(anyString())).thenReturn(data);
		return mockRequest;
	}

	private FilterChain nopFilterChain() {
		return mock(FilterChain.class);
	}
}
