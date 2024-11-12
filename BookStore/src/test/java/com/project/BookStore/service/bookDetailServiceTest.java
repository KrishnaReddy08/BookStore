package com.project.BookStore.service;

import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.exception.BookNotFoundException;
import com.project.BookStore.exception.InvalidPropertiesException;
import com.project.BookStore.model.book;
import com.project.BookStore.repository.bookRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class bookDetailServiceTest {
    @InjectMocks
    private bookDetailService service;
    @Mock
    private bookRepo bookRepo;
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
        book=null;
    }


    @Test public void testAddBook() {
        when(bookRepo.existsByTitle(book.getTitle())).thenReturn(false);
        when(bookRepo.save(any(book.class))).thenReturn(book);
        ResponseEntity<responseStructure<book>> response = service.addBook(book);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Added Successfully", response.getBody().getMessage()); assertEquals(book, response.getBody().getData());

        when(bookRepo.existsByTitle(book.getTitle())).thenReturn(true);
        assertThrows(InvalidPropertiesException.class, () -> {
            service.addBook(book);
        });
    }

    @Test
    void TestViewAllBooks() {
        when(bookRepo.findAll()).thenReturn(Arrays.asList(book));
        ResponseEntity<responseStructure<List<book>>> response = service.viewAllBooks();
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Books Found",response.getBody().getMessage());
        assertFalse(response.getBody().getData().isEmpty());

        when(bookRepo.findAll()).thenReturn(Collections.emptyList());
        assertThrows(BookNotFoundException.class ,() -> {
            service.viewAllBooks();
        });
    }

    @Test
    void viewBookById() {
        when(bookRepo.findById(1)).thenReturn(Optional.of(book));
        ResponseEntity<responseStructure<book>> response = service.viewBookById(1);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Book Found",response.getBody().getMessage());
        assertEquals(book,response.getBody().getData());

        when(bookRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class,()->{
            service.deleteBookById(1);
        });
    }

    @Test
    void viewBookByTitle() {
        when(bookRepo.findByTitle("title")).thenReturn(Optional.of(book));
        ResponseEntity<responseStructure<book>> response = service.viewBookByTitle("title");
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Book Found",response.getBody().getMessage());
        assertEquals(book,response.getBody().getData());

        when(bookRepo.findByTitle("title")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class,() ->{
            service.viewBookByTitle("title");
        });
    }

    @Test
    void updateBook() {
        when(bookRepo.findById(1)).thenReturn(Optional.of(book));
        when(bookRepo.existsByTitle("title")).thenReturn(false);
        when(bookRepo.save(any(book.class))).thenReturn(book);
        ResponseEntity<responseStructure<book>> response = service.updateBook(book,1);
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Updated Successfully",response.getBody().getMessage());
        assertEquals(book,response.getBody().getData());

        when(bookRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class,()->{
            service.updateBook(book,1);
        });

        when(bookRepo.existsByTitle(book.getTitle())).thenReturn(true);
        assertThrows(InvalidPropertiesException.class,()->{
            service.updateBook(book,1);
        });

    }

    @Test
    void deleteBookById() {
        when(bookRepo.findById(1)).thenReturn(Optional.of(book));
        ResponseEntity<responseStructure<book>> response = service.deleteBookById(1);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Deleted Successfully",response.getBody().getMessage());
        assertEquals(book,response.getBody().getData());

        when(bookRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class,()->{
            service.deleteBookById(1);
        });
    }

    @Test
    void deleteBookByTitle() {
        when(bookRepo.findByTitle("title")).thenReturn(Optional.of(book));
        ResponseEntity<responseStructure<book>> response = service.deleteBookByTitle("title");
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Deleted Successfully",response.getBody().getMessage());
        assertEquals(book,response.getBody().getData());

        when(bookRepo.findByTitle("title")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class,()->{
            service.deleteBookByTitle("title");
        });
    }
}