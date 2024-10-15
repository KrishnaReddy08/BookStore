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
							auth.requestMatchers("/addbook","/deletebook/**","/deletebookbytitle/**","/updatebook/**",
									             "/viewallcustomers","/viewcustomerbyname","/viewcustomer/**","/addcustomer",
									             "/deletecustomer/**","/deletecustomerbyname/**",
									             "/updateuser/**","/addnewuser","/admin/**")
												.hasRole("ADMIN");
							auth.requestMatchers("/viewallbooks","/viewbook/**","/viewbookbytitle/**","/viewcustomerdetails","/updateorder/**",
												 "/deleteorder/**","/placeorder","/vieworder/**","/viewallorders","/updatecustomer","/updatecurrentuser")
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
		return provider;
	}
}
