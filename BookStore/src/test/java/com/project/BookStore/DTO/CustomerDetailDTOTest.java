package com.project.BookStore.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDetailDTOTest {
    private  CustomerDetailDTO DTO;

    @Test
    public void TestCustomerDTO(){
        DTO = new CustomerDetailDTO();
        DTO.setCustomerId(1);
        DTO.setEmail("krishna@gmail.com");
        DTO.setName("name");
        assertEquals("name",DTO.getName());
        assertEquals("krishna@gmail.com",DTO.getEmail());
        assertEquals(1,DTO.getCustomerId());
    }

}