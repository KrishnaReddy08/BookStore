package com.project.BookStore.config;

import com.project.BookStore.JWT.JWTFilter;
import com.project.BookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatchers;

@Configuration
@EnableWebSecurity
public class securityConfiguration{
	@Autowired
	private UserService service;

	@Autowired
	private JWTFilter jwtFilter;

	@Bean
    SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
		return http
				.csrf(customizer->customizer.disable())
				.authorizeHttpRequests(
						auth->{
							auth.requestMatchers("/","/login").permitAll();
							auth.requestMatchers("/admin/**")
												.hasRole("ADMIN");
							auth.requestMatchers("/viewcurrentuser","/updatecurrentuser","/deletecurrentuser",
												 "/deleteorder/**","/viewallorders","/vieworder/**","/updateorder/**",
												 "/viewcurrentcustomer","/updatecurrentcustomer","/deletecurrentcustomer",
												 "/viewallbooks","/viewbook/**","/viewbookbytitle/**")
							                    .hasAnyRole("ADMIN","USER");
							auth.anyRequest().authenticated();
						})
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();		
	}

    @Bean
    AuthenticationProvider authentication() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
		provider.setUserDetailsService(service);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuraion) throws Exception {
		return configuraion.getAuthenticationManager();
	}
    
}
