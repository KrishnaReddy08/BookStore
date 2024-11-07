package com.project.BookStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.JWT.jwtservice;
import com.project.BookStore.model.userCredentials;
import com.project.BookStore.repository.userCredentialsRepo;
import com.project.BookStore.service.UserService;
import com.project.BookStore.service.userCredentialsService;
import org.assertj.core.api.Assertions;
import org.awaitility.reflect.WhiteboxImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(userCredentialsController.class)
@AutoConfigureMockMvc
class userCredentialsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private userCredentialsService service;
    @MockBean
    private userCredentialsRepo repo;

    @MockBean
    private com.project.BookStore.JWT.jwtservice jwtservice;
    @MockBean
    private UserService userService;


    private responseStructure<userCredentials> structure;
    private ResponseEntity<responseStructure<userCredentials>> response;

    private responseStructure<List<userCredentials>> liststructure;
    private ResponseEntity<responseStructure<List<userCredentials>>> listresponse;

    private userCredentials credentials;
    private List<userCredentials> credentialsList;

    @BeforeEach
    void setUp() {
        credentials = new userCredentials();
        credentialsList = new ArrayList<>();
        credentialsList.add(credentials);

        structure = new responseStructure<>();
        response= new ResponseEntity<responseStructure<userCredentials>>(structure, HttpStatus.ACCEPTED);

        liststructure = new responseStructure<>();
        listresponse = new ResponseEntity<responseStructure<List<userCredentials>>>(liststructure,HttpStatus.OK);

    }

    @AfterEach
    void tearDown() {
        credentials=null;
        listresponse=null;
        liststructure=null;
        response=null;
        structure=null;
        credentialsList=null;
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewUserAdmin() throws Exception {
        when(service.viewUserAdmin(1)).thenReturn(new ResponseEntity<responseStructure<userCredentials>>(structure,HttpStatus.OK));

        mockMvc.perform(get("/admin/viewuser/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewCurrentUser() throws Exception {
        when(service.viewCurrentUser()).thenReturn(new ResponseEntity<responseStructure<userCredentials>>(structure,HttpStatus.OK));

        mockMvc.perform(get("/viewcurrentuser").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void addNewUser() throws Exception {
        when(service.addNewUser(any(userCredentials.class))).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String stringUserCredentials = objectMapper.writeValueAsString(credentials);

        mockMvc.perform(post("/admin/addnewuser").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringUserCredentials))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void updateUser() throws Exception {
        when(service.UpdateUser(eq(1),any(userCredentials.class))).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String stringUserCredentials = objectMapper.writeValueAsString(credentials);

        mockMvc.perform(put("/admin/updateuser/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringUserCredentials))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void testUpdateUser() throws Exception {
        when(service.UpdateCurrentUser(any(userCredentials.class))).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String stringUserCredentials = objectMapper.writeValueAsString(credentials);

        mockMvc.perform(put("/updatecurrentuser").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringUserCredentials))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void deleteUserAdmin() throws Exception {
        when(service.deleteUser(1)).thenReturn(response);

        mockMvc.perform(delete("/admin/deleteuser/1").with(csrf()))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void deleteUser() throws Exception {
        when(service.deleteCurrentUser()).thenReturn(response);

        mockMvc.perform(delete("/deletecurrentuser").with(csrf()))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewAllUsers() throws Exception {
        when(service.viewAllUsers()).thenReturn(listresponse);

        mockMvc.perform(get("/admin/viewallusers").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void login() throws Exception {
        when(service.verify(any(userCredentials.class))).thenReturn(new ResponseEntity<responseStructure<String>>(new responseStructure<String>(),HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        String stringUserCredentials = objectMapper.writeValueAsString(credentials);

        mockMvc.perform(post("/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringUserCredentials))
                .andExpect(status().isOk());
    }
}