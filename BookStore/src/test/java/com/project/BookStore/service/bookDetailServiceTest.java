package com.project.BookStore.service;

import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.exception.InvalidPropertiesException;
import com.project.BookStore.model.book;
import com.project.BookStore.repository.bookRepo;
import com.project.BookStore.repository.orderDetailsRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.any;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class bookDetailServiceTest {
    @InjectMocks
    private bookDetailService service;
    @Mock
    private bookRepo bookRepo;
    @Mock
    private orderDetailsRepo orderRepo;
    private book book;
    @BeforeEach void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new book(); book.setBookId(1);
        book.setAuthor("Author");
        book.setTitle("Title");
        book.setPrice(20.0F);
        book.setQuantity(10);
    }

    @AfterEach
    void tearDown() {
    }


    @Test public void testAddBook_Success() {
        when(bookRepo.existsByTitle(book.getTitle())).thenReturn(false);
        when(bookRepo.save(any(book.class))).thenReturn(book);
        ResponseEntity<responseStructure<book>> response = service.addBook(book);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Added Successfully", response.getBody().getMessage()); assertEquals(book, response.getBody().getData()); }

    @Test
    public void testAddBook_Failure() {
        when(bookRepo.existsByTitle(book.getTitle())).thenReturn(true);
        assertThrows(InvalidPropertiesException.class, () -> {
            service.addBook(book);
        });
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
    void deleteBookById() {
    }

    @Test
    void deleteBookByTitle() {
    }
}