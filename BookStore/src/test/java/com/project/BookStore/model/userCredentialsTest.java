package com.project.BookStore.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

public class userCredentialsTest {
    private userCredentials user;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    public void beforeAll() {
        user = new userCredentials();
        encoder = new BCryptPasswordEncoder(10);
    }

    @Test
    public void testGetterSetters(){
        user.setPassword("krishna");
        user.setUsername("krishna");
        user.setCustomerId(1);
        Assertions.assertEquals("krishna",user.getUsername());
        Assertions.assertEquals(1,user.getCustomerId());
        Assertions.assertEquals(true, encoder.matches("krishna", user.getPassword()));
    }
    @Test
    public void testPasswordEncoding(){
        String password = "krishna";
        user.setPassword(password);
        Assertions.assertEquals(false,encoder.matches("wrongpassword", user.getPassword()));
        Assertions.assertEquals(true,encoder.matches(password, user.getPassword()));
    }
    @Test
    public void testCustomer(){
        customer customer = new customer();
        customer.setCustomerId(1);
        customer.setEmail("krishna08@gmail.com");
        user.setCustomer(customer);
        Assertions.assertEquals(customer,user.getCustomer());
    }
    @Test
    public void testRoles(){
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        user.setRoles(roles);
        Assertions.assertEquals(Role.ADMIN,user.getRoles().stream().findFirst().get());
    }
}
