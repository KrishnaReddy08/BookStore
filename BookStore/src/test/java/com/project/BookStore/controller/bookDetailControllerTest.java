package com.project.BookStore.controller;

import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.model.book;
import com.project.BookStore.repository.bookRepo;
import com.project.BookStore.service.bookDetailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(bookDetailController.class)
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

    @BeforeEach
    void setUp() {
        book = new book();
        structure = new responseStructure<book>();
        response = new ResponseEntity<responseStructure<book>>(structure,HttpStatus.ACCEPTED);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addBook() throws Exception {
        when(service.addBook(book)).thenReturn(response);
        mockMvc.perform(post("/admin/addbook"))
                .andDo(print()).andExpect(status().isAccepted());

    }

    @Test
    void viewAllBooks() {
    }

    @Test
    void viewBookById() {
    }

    @Test
    void viewBookByTitle() {
    }

    @Test
    void updateBook() {
    }

    @Test
    void deleteBook() {
    }

    @Test
    void deleteBookByTitle() {
    }
}