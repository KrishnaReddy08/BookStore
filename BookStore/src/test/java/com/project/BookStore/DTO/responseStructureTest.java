package com.project.BookStore.DTO;

import com.project.BookStore.model.customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class responseStructureTest {

    customer customer = new customer();
    responseStructure<customer> response = new responseStructure<>();

    @AfterEach
    void tearDown() {
        response=null;
        customer=null;
    }
    @Test
    public void  TestGetterSetters(){
        response.setMessage("message");
        response.setStatus_code(HttpStatus.OK.value());
        response.setData(customer);

        assertEquals(HttpStatus.OK.value(),response.getStatus_code());
        assertEquals("message",response.getMessage());
        assertEquals(customer,response.getData());
    }
}