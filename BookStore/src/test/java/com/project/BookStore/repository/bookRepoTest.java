package com.project.BookStore.repository;

import com.project.BookStore.model.book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class bookRepoTest {
    @Autowired
    private bookRepo repo;
    private book book;

    @BeforeEach
    void setUp() {
        book = new book();
        book.setBookId(1);
        book.setAuthor("author");
        book.setTitle("title1");
        book.setPrice(50.70F);
        book.setQuantity(2);
        repo.save(book);
    }

    @AfterEach
    void tearDown() {
        repo.deleteAll();
        book=null;
    }

    @Test
    public void TestFindByTitle(){
        book foundBook = repo.findByTitle("title1").get();
        Assertions.assertEquals("author",foundBook.getAuthor());
    }
    @Test
    public  void TestExistsByTitle(){
        Assertions.assertEquals(true,repo.existsByTitle("title1"));
    }
}
