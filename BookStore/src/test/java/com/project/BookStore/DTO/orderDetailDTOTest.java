package com.project.BookStore.DTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class orderDetailDTOTest {
    OrderDetailDTO DTO = new OrderDetailDTO();
    @Test
    public void TestGetterSetters(){
        DTO.setOrderId(1);
        DTO.setName("name");
        DTO.setCustomerId(1);
        DTO.setQuantity(23);
        DTO.setPrice(67.90F);
        DTO.setBookId(2);
        DTO.setTitle("title");

        assertEquals(1,DTO.getOrderId());
        assertEquals("name",DTO.getName());
        assertEquals(1,DTO.getCustomerId());
        assertEquals(23,DTO.getQuantity());
        assertEquals(67.90F,DTO.getPrice());
        assertEquals(2,DTO.getBookId());
        assertEquals("title",DTO.getTitle());
    }
}
