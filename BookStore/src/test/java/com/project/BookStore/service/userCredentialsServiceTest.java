package com.project.BookStore.service;

import com.project.BookStore.AuthenticatedUser.AuthenticatedUserDetails;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.JWT.jwtservice;
import com.project.BookStore.exception.InvalidRequestException;
import com.project.BookStore.exception.UserNotFoundException;
import com.project.BookStore.model.Role;
import com.project.BookStore.model.customer;
import com.project.BookStore.model.userCredentials;
import com.project.BookStore.repository.userCredentialsRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

class userCredentialsServiceTest {

    @InjectMocks
    private  userCredentialsService service;
    private userCredentials credentials;
    @Mock
    private userCredentialsRepo repo;
    private customer customer;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private jwtservice jwtservice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new customer();
        customer.setCustomerId(1);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        credentials = new userCredentials();
        credentials.setCustomerId(1);
        credentials.setRoles(roles);
        credentials.setUsername("username");
        credentials.setPassword("password");
        credentials.setCustomer(customer);
    }

    @AfterEach
    void tearDown() {
        credentials=null;
        repo.deleteAll();
    }

    @Test
    void initAdminUser() {
    }

    @Test
    void viewUserAdmin() {
        when(repo.findById(1)).thenReturn(Optional.of(credentials));
        ResponseEntity<responseStructure<userCredentials>> response = service.viewUserAdmin(1);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("USER FOUND",response.getBody().getMessage());
        assertEquals(credentials,response.getBody().getData());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->{
            service.viewUserAdmin(1);
        });
    }

    @Test
    void viewCurrentUser() {
        when(repo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        when(repo.findById(1)).thenReturn(Optional.of(credentials));
        ResponseEntity<responseStructure<userCredentials>> response = service.viewCurrentUser();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("USER FOUND",response.getBody().getMessage());
        assertEquals(credentials,response.getBody().getData());
    }

    @Test
    void addNewUser() {
        when(repo.existsById(credentials.getCustomerId())).thenReturn(false);
        when(repo.save(any(userCredentials.class))).thenReturn(credentials);
        ResponseEntity<responseStructure<userCredentials>> response = service.addNewUser(credentials);

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Added User",response.getBody().getMessage());
        assertEquals(credentials,response.getBody().getData());

        when(repo.existsById(credentials.getCustomerId())).thenReturn(true);
        assertThrows(InvalidRequestException.class,()->{
            service.addNewUser(credentials);
        });
    }

    @Test
    void updateUser() {
        when(repo.findById(1)).thenReturn(Optional.of(credentials));
        when(repo.save(any(userCredentials.class))).thenReturn(credentials);
        ResponseEntity<responseStructure<userCredentials>> response = service.UpdateUser(1,credentials);

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("User Credentials Updated",response.getBody().getMessage());
        assertEquals(credentials.getUsername(),response.getBody().getData().getUsername());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->{
            service.UpdateUser(1,credentials);
        });
    }

    @Test
    void updateCurrentUser() {
        when(repo.findById(1)).thenReturn(Optional.of(credentials));
        when(repo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        when(repo.save(any(userCredentials.class))).thenReturn(credentials);
        ResponseEntity<responseStructure<userCredentials>> response = service.UpdateCurrentUser(credentials);

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("User Credentials Updated",response.getBody().getMessage());
        assertEquals(credentials.getUsername(),response.getBody().getData().getUsername());

    }

    @Test
    void deleteUser() {
        when(repo.findById(1)).thenReturn(Optional.of(credentials));
        ResponseEntity<responseStructure<userCredentials>> response = service.deleteUser(1);

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("User Deleted",response.getBody().getMessage());
        assertEquals(credentials,response.getBody().getData());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->{
            service.deleteUser(1);
        });
    }

    @Test
    void deleteCurrentUser() {
        when(repo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        when(repo.findById(1)).thenReturn(Optional.of(credentials));
        ResponseEntity<responseStructure<userCredentials>> response = service.deleteCurrentUser();

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("User Deleted",response.getBody().getMessage());
        assertEquals(credentials,response.getBody().getData());
    }

    @Test
    void viewAllUsers() {
        when(repo.findAll()).thenReturn(java.util.List.of(credentials));
        ResponseEntity<responseStructure<java.util.List<userCredentials>>> response = service.viewAllUsers();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Users Found",response.getBody().getMessage());
        assertEquals(java.util.List.of(credentials),response.getBody().getData());

        when(repo.findAll()).thenReturn(List.of());
        assertThrows(UserNotFoundException.class,()->{
            service.viewAllUsers();
        });
    }

    @Test
    void verify() {
        when(repo.findByUsername(credentials.getUsername())).thenReturn(Optional.of(credentials));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));

        String Token = "metbmklmbpwih4jmhpom4bi4";
        when(jwtservice.generateToken(credentials.getUsername())).thenReturn(Token);

        ResponseEntity<responseStructure<String>> response = service.verify(credentials);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("failed", response.getBody().getMessage());
    }
}