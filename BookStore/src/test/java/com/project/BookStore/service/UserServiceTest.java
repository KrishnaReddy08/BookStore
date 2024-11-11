package com.project.BookStore.service;

import com.project.BookStore.exception.UserNotFoundException;
import com.project.BookStore.model.userCredentials;
import com.project.BookStore.repository.userCredentialsRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class UserServiceTest {

    @Mock
    private userCredentialsRepo repo;

    @InjectMocks
    private UserService userService;
    private userCredentials credentials;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        credentials = new userCredentials();


    }

    @AfterEach
    void tearDown() {
        credentials = null;
    }

    @Test
    void testLoadUserByUsername() {
        when(repo.findByUsername("username")).thenReturn(java.util.Optional.of(credentials));
        assertEquals(credentials.getUsername(), userService.loadUserByUsername("username").getUsername());

        when(repo.findByUsername("username")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userService.loadUserByUsername("username");
        });
    }
}