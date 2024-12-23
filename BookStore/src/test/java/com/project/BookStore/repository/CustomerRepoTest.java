package com.project.BookStore.repository;

import com.project.BookStore.model.Role;
import com.project.BookStore.model.customer;
import com.project.BookStore.model.userCredentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
public class CustomerRepoTest {
    @Autowired
    private customerRepo customerRepo;
    private customer customer;
    @Autowired
    private  userCredentialsRepo credentialsRepo;
    private userCredentials credentials;

    @BeforeEach
    void setUp() {
        credentials = new userCredentials();
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        customer = new customer();
        customer.setCustomerId(1);
        customer.setName("name");
        customer.setEmail("name123@gmail.com");
        credentials.setPassword("password");
        credentials.setCustomerId(1);
        credentials.setUsername("username");
        credentials.setCustomer(customer);
        credentialsRepo.save(credentials);
        customerRepo.save(customer);
    }

    @AfterEach
    void tearDown() {
        customerRepo.deleteAll();
        credentialsRepo.deleteAll();
        credentials=null;
        customer=null;
    }

    @Test
    public void TestFindByName(){
        List<customer> cust = customerRepo.findByName("name").get();
        Assertions.assertEquals("name123@gmail.com",cust.get(0).getEmail());
    }


}