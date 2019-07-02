package com.codecamos.timetracking.config;

import com.codecamos.timetracking.config.auth.TokenAuthenticationFilter;
import com.codecamos.timetracking.service.ITokenService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMongoAuditing
@EnableGlobalMethodSecurity(prePostEnabled = true, order = Ordered.HIGHEST_PRECEDENCE)
public class Config extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	private Environment env;
	private ITokenService tokenService;

	@Autowired
	public Config(Environment env, ITokenService tokenService) {
		this.env = env;
		this.tokenService = tokenService;
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		if (Resource.isInDevOrStage(this.env.getActiveProfiles())) {
			http
					.csrf().disable()
					.logout().disable()
					.formLogin().disable()
					.addFilterBefore(new TokenAuthenticationFilter(this.tokenService), BasicAuthenticationFilter.class)
					.authorizeRequests()
					.anyRequest().permitAll();
		}
		else {
			http
					.csrf().disable()
					.logout().disable()
					.formLogin().disable()
					.addFilterBefore(new TokenAuthenticationFilter(this.tokenService), BasicAuthenticationFilter.class)
					.authorizeRequests()
					.anyRequest().fullyAuthenticated();
		}
	}

	@Bean
	public ObjectMapper jsonMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper;
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.APPLICATION_JSON);
	}

	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {
		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}
}
