package com.project.BookStore.repository;

import com.project.BookStore.model.Role;
import com.project.BookStore.model.userCredentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
@Transactional
@DataJpaTest
public class userCredentialsRepoTest{
    @Autowired
    private userCredentialsRepo repo;
    private userCredentials credentials;

    @BeforeEach
    void setUp() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        credentials = new userCredentials();
        credentials.setPassword("password");
        credentials.setCustomerId(1);
        credentials.setUsername("userName");
        credentials.setRoles(roles);
        repo.save(credentials);
    }

    @AfterEach
    void tearDown() {
        repo.deleteAll();
        credentials=null;
    }

    @Test
    public void TestfindByName(){
        userCredentials userCredentials = repo.findByUsername("userName").get();
        Assertions.assertEquals("userName",userCredentials.getUsername());
    }

}
