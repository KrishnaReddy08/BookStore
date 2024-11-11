package com.project.BookStore.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.BookStore.model.Role;
import com.project.BookStore.model.userCredentials;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class userDetailConfigTest {
	private userCredentials credentials;
	
	@Autowired
	private userDetailConfig config;

	Set<Role> roles;
	 
	@BeforeEach
	void setUp() throws Exception {
		roles = new HashSet<Role>();
		roles.add(Role.ADMIN);
		credentials = new userCredentials();
		credentials.setUsername("username");
		credentials.setPassword("password");
		credentials.setRoles(roles);
		config = new userDetailConfig(credentials);
	}

	@AfterEach
	void tearDown() throws Exception {
		credentials = null;
	}
	

	@Test
	void test() {
		assertEquals("password",config.getPassword());
		assertEquals("username",config.getUsername());
		assertEquals("[ROLE_ADMIN]",config.getAuthorities().toString());
	}

}
