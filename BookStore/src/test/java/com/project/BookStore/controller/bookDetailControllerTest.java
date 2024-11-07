package com.project.BookStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.JWT.jwtservice;

import com.project.BookStore.model.book;
import com.project.BookStore.repository.bookRepo;

import com.project.BookStore.service.UserService;
import com.project.BookStore.service.bookDetailService;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockBean
    private jwtservice jwtservice;

    @MockBean
    private UserService userService;

    private book book;

    private responseStructure<book> structure;
    private ResponseEntity<responseStructure<book>> response;

    private responseStructure<List<book>> Liststructure;
    private ResponseEntity<responseStructure<List<book>>> Listresponse;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        book = new book();
        Liststructure = new responseStructure<>();
        Listresponse = new ResponseEntity<responseStructure<List<book>>>(Liststructure,HttpStatus.OK);
        structure = new responseStructure<book>();
        response = new ResponseEntity<responseStructure<book>>(structure,HttpStatus.ACCEPTED);


    }

    @AfterEach
    void tearDown() {
        book = null;
        Liststructure = null;
        Listresponse = null;
        structure =null;
        response=null;
    }


    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void addBook() throws Exception {
        when(service.addBook(any(book.class))).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/admin/addbook").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                        .andDo(print()).andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewAllBooks() throws Exception {
        when(service.viewAllBooks()).thenReturn(Listresponse);

        mockMvc.perform(get("/viewallbooks").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewBookById() throws Exception {
        when(service.viewBookById(1)).thenReturn(new ResponseEntity<responseStructure<book>>(structure,HttpStatus.OK));

        mockMvc.perform(get("/viewbook/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewBookByTitle() throws Exception {
        when(service.viewBookByTitle("title")).thenReturn(new ResponseEntity<responseStructure<book>>(structure,HttpStatus.OK));

        mockMvc.perform(get("/viewbookbytitle/title").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void updateBook() throws Exception {
        when(service.updateBook(any(book.class),eq(1))).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(put("/admin/updatebook/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                        .andDo(print())
                        .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void deleteBook() throws Exception {
        when(service.deleteBookById(1)).thenReturn(response);

        mockMvc.perform(delete("/admin/deletebook/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void deleteBookByTitle() throws Exception {
        when(service.deleteBookByTitle("title")).thenReturn(response);

        mockMvc.perform(delete("/admin/deletebookbytitle/title").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isAccepted());
    }
}