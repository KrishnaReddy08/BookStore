package com.project.BookStore.config;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.BookStore.model.Role;
import com.project.BookStore.model.userCredentials;

@Configuration
public class userDetailConfig implements UserDetails {
	
	@Autowired
	private userCredentials credentials;
	
	public userDetailConfig() {
	}
	
	public userDetailConfig(userCredentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = credentials.getRoles();
		return roles.stream()
							.map(role->new SimpleGrantedAuthority("ROLE_"+role.name()))
							.collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		return credentials.getPassword();
	}

	@Override
	public String getUsername() {
		return credentials.getUsername();
	}
}
