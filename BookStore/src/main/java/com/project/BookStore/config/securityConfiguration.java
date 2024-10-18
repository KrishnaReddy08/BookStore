package com.project.BookStore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class securityConfiguration{
	@Autowired
	private UserDetailsService service;

	@Bean
    SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
		return http
				.csrf(customizer->customizer.disable())
				.authorizeHttpRequests(
						auth->{
							auth.requestMatchers("/").permitAll();
							auth.requestMatchers("/admin/**")
												.hasRole("ADMIN");
							auth.requestMatchers("/viewcurrentuser","/updatecurrentuser","/deletecurrentuser",
												 "/deleteorder/**","/viewallorders","/vieworder/**","/updateorder/**",
												 "/viewcurrentcustomer","/updatecurrentcustomer","/deletecurrentcustomer",
												 "/viewallbooks","/viewbook/**","/viewbookbytitle/**")
							                    .hasAnyRole("ADMIN","USER");
						})
				.authorizeHttpRequests(request->request.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();		
	}

    @Bean
    AuthenticationProvider authentication() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
		provider.setUserDetailsService(service);
		System.out.println(provider.getUserCache());
		return provider;
	}
    
    
}
