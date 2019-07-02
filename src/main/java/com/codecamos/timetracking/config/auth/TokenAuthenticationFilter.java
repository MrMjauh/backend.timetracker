package com.codecamos.timetracking.config.auth;

import com.codecamos.timetracking.model.persistance.User;
import com.codecamos.timetracking.service.ITokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private ITokenService tokenService;

	public TokenAuthenticationFilter(ITokenService tokenService) {
		Assert.notNull(tokenService, "Can not be null");
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String data = request.getHeader(SecurityResource.X_AUTH_KEY);
		if (!StringUtils.hasText(data)) {
			filterChain.doFilter(request,response);
			return;
		}

		User user = this.tokenService.getUserFromToken(data);
		if (user == null) {
			filterChain.doFilter(request,response);
			return;
		}
		AuthenticatedUser authenticatedUser = AuthenticatedUser.from(user);

		String[] authorities;
		if (user.getRoles() == null) {
			authorities = new String[]{};
		} else {
			authorities = user.getRoles().toArray(new String[]{});
		}

		Authentication authentication = new PreAuthenticatedAuthenticationToken(
				authenticatedUser,
				data,
				AuthorityUtils.createAuthorityList(authorities)
		);

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

		filterChain.doFilter(request, response);
	}
}
