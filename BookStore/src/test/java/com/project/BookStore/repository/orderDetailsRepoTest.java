package com.project.BookStore.repository;

import com.project.BookStore.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@DataJpaTest
public class orderDetailsRepoTest {
    @Autowired
    private orderDetailsRepo repo;
    private orderDetails order;
    @Autowired
    private  customerRepo customerRepo;
    private  customer customer;
    @Autowired
    private bookRepo bookRepo;
    private book book;
    @Autowired
    private userCredentialsRepo credentialsRepo;
    private userCredentials credentials;


    @BeforeEach
    void setUp() {
        credentials = new userCredentials();
        Set<Role> roles = new HashSet<>(); roles.add(Role.ADMIN);
        credentials.setPassword("password");
        credentials.setCustomerId(4);
        credentials.setUsername("username");
        credentials.setRoles(roles);

        customer = new customer();
        customer.setName("name");
        customer.setEmail("krishna@gmail.com");
        customer.setCustomerId(4);
        customer.setUserCredentials(credentials);
        credentials.setCustomer(customer);
        credentialsRepo.save(credentials);
        customerRepo.save(customer);

        book = new book();
        book.setBookId(1);
        book.setAuthor("author");
        book.setTitle("title1");
        book.setPrice(50.70F);
        book.setQuantity(2);
        bookRepo.save(book);

        order = new orderDetails();
        order.setOrderId(1);
        order.setQuantity(2);
        order.setBook(book);
        order.setCustomer(customer);
        repo.save(order);
    }

    @AfterEach
    void tearDown() {
        repo.deleteAll();
        bookRepo.deleteAll();
        customerRepo.deleteAll();
        credentialsRepo.deleteAll();
        book=null;
        customer=null;
        credentials=null;
        order=null;

    }

    @Test
    public void TestFindByCustomer(){
        List<orderDetails> foundOrder = repo.findByCustomer(customer).get();
        Assertions.assertEquals(2,foundOrder.get(0).getQuantity());
    }
}
