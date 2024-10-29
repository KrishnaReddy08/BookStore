package com.project.BookStore.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class customerTest {
    private customer customer;
    @BeforeEach
    void setUp() {
        customer = new customer();
    }

    @Test
    public void TestGetterSetters(){
        customer.setCustomerId(1);
        customer.setEmail("krishna@gmail.com");
        customer.setName("krishna");
        Assertions.assertEquals(1,customer.getCustomerId());
        Assertions.assertEquals("krishna",customer.getName());
        Assertions.assertEquals("krishna@gmail.com",customer.getEmail());
    }
    @Test
    public void TestUserCredentials(){
        userCredentials credentials = new userCredentials();
        credentials.setUsername("krishna");
        customer.setUserCredentials(credentials);
        Assertions.assertEquals("krishna",customer.getUserCredentials().getUsername());
    }
    @Test
    public void TestOrderDetails(){
        orderDetails order = new orderDetails();
        order.setOrderId(1);
        List<orderDetails> orders = new ArrayList<>();
        orders.add(order);
        customer.setOrderdetails(orders);
        Assertions.assertEquals(order,customer.getOrderdetails().get(0));
    }
}
