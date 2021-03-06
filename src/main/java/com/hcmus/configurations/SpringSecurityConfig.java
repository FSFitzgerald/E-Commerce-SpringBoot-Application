package com.hcmus.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("admin").password("{noop}admin").roles("admin");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.httpBasic()
		.and()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/products").hasRole("admin")
		.antMatchers(HttpMethod.PUT, "/products/**").hasRole("admin")
		.antMatchers(HttpMethod.DELETE, "/products/**").hasRole("admin")
		.and()
		.csrf().disable()
		.formLogin().disable();
	}

}
