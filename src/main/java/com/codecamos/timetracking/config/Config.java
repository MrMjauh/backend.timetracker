package com.codecamos.timetracking.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class Config extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Autowired
	Environment env;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		if (Resource.isInDevOrStage(this.env.getActiveProfiles())) {
			http
					.csrf().disable()
					.logout().disable()
					.formLogin().disable()
					.authorizeRequests()
					.antMatchers("*").permitAll();
		}
		else {
			http
					.csrf().disable()
					.logout().disable()
					.formLogin().disable()
					.authorizeRequests()
					.antMatchers(HttpMethod.GET, "/hello").permitAll()
					.antMatchers(HttpMethod.GET, "/runtime").permitAll()
					.anyRequest().fullyAuthenticated();
		}
	}

	@Bean
	public ObjectMapper jsonMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper;
	}

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
