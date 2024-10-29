package com.project.BookStore.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class orderDetailsTest {
    private orderDetails order;
    @BeforeEach
    void setUp() {
        order = new orderDetails();
    }

    @Test
    public  void TestGetterSetters(){
        order.setOrderId(1);
        order.setQuantity(23);
        Assertions.assertEquals(1,order.getOrderId());
        Assertions.assertEquals(23,order.getQuantity());
    }
    @Test
    public void TestBook(){
        book book = new book();
        book.setBookId(23);
        order.setBook(book);
        Assertions.assertEquals(23,order.getBook().getBookId());
    }
    @Test
    public void TestCustomer(){
        customer customer = new customer();
        customer.setName("Customer");
        order.setCustomer(customer);
        Assertions.assertEquals("Customer",order.getCustomer().getName());
    }
}
