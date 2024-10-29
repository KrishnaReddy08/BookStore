package com.project.BookStore.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class bookTest {
    private book book;
    @BeforeEach
    void setUp() {
        book = new book();
    }

    @Test
    public void TestGetterSetters(){
        book.setBookId(1);
        book.setAuthor("author");
        book.setQuantity(23);
        book.setTitle("Title");
        book.setPrice(90.09F);
        Assertions.assertEquals(1,book.getBookId());
        Assertions.assertEquals("author",book.getAuthor());
        Assertions.assertEquals("Title",book.getTitle());
        Assertions.assertEquals(90.09F,book.getPrice());
        Assertions.assertEquals(23,book.getQuantity());
    }
    @Test
    public void TestOrderDetails(){
        orderDetails order = new orderDetails();
        order.setOrderId(2);
        List<orderDetails> orders = new ArrayList<>();
        orders.add(order);
        book.setOrderdetails(orders);
        Assertions.assertEquals(2,book.getOrderdetails().get(0).getOrderId());
    }
}
