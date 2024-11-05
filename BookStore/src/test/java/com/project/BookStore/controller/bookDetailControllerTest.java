package com.project.BookStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.model.Role;
import com.project.BookStore.model.book;
import com.project.BookStore.repository.bookRepo;
import com.project.BookStore.service.bookDetailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(bookDetailController.class)
@AutoConfigureMockMvc
class bookDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private bookDetailService service;
    @MockBean
    private bookRepo repo;

    private book book;
    private responseStructure<book> structure;
    private ResponseEntity<responseStructure<book>> response;
    private responseStructure<List<book>> Liststructure;
    private ResponseEntity<responseStructure<List<book>>> Listresponse;

    @BeforeEach
    void setUp() {
        book = new book();
        Liststructure = new responseStructure<>();
        Listresponse = new ResponseEntity<responseStructure<List<book>>>(Liststructure,HttpStatus.OK);
        structure = new responseStructure<book>();
        response = new ResponseEntity<responseStructure<book>>(structure,HttpStatus.ACCEPTED);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void addBook() throws Exception {
        when(service.addBook(any(book.class))).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/admin/addbook").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)) .andDo(print())
                        .andDo(print()).andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewAllBooks() throws Exception {
        when(service.viewAllBooks()).thenReturn(Listresponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(get("/viewallbooks").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)) .andDo(print())
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewBookById() {
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewBookByTitle() {
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void updateBook() {
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void deleteBook() {
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void deleteBookByTitle() {
    }
}