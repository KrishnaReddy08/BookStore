package com.project.BookStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BookStore.DTO.OrderDetailDTO;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.JWT.jwtservice;
import com.project.BookStore.model.book;
import com.project.BookStore.model.customer;
import com.project.BookStore.model.orderDetails;
import com.project.BookStore.repository.bookRepo;
import com.project.BookStore.repository.orderDetailsRepo;
import com.project.BookStore.service.UserService;
import com.project.BookStore.service.bookDetailService;
import com.project.BookStore.service.orderDetailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(orderDetailController.class)
@AutoConfigureMockMvc
class orderDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private orderDetailService service;
    @MockBean
    private orderDetailsRepo repo;

    @MockBean
    private jwtservice jwtservice;

    @MockBean
    private UserService userService;

    private OrderDetailDTO orderDetails;

    private responseStructure<OrderDetailDTO> structure;
    private ResponseEntity<responseStructure<OrderDetailDTO>> response;

    private responseStructure<List<OrderDetailDTO>> Liststructure;
    private ResponseEntity<responseStructure<List<OrderDetailDTO>>> Listresponse;

    private List<OrderDetailDTO> orderDetailsList;

    private customer customer;

    @BeforeEach
    void setUp() {
        customer = new customer();
        orderDetails = new OrderDetailDTO();
        structure = new responseStructure<>();
        response = new ResponseEntity<responseStructure<OrderDetailDTO>>(structure,HttpStatus.ACCEPTED);

        Liststructure = new responseStructure<>();
        Listresponse = new ResponseEntity<responseStructure<List<OrderDetailDTO>>>(Liststructure,HttpStatus.OK);
    }

    @AfterEach
    void tearDown() {
        customer = null;
        orderDetails = null;
        structure=null;
        response=null;
        Listresponse=null;
        Liststructure=null;
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void placeAnOrder() throws Exception {
        when(service.placeOrderAdmin(any(OrderDetailDTO.class))).thenReturn(response);

        ObjectMapper objectMapper =  new ObjectMapper();
        String stringOrderDetails = objectMapper.writeValueAsString(orderDetails);

        mockMvc.perform(post("/admin/placeorder").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringOrderDetails))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void placeOrder() throws Exception {
        when(service.placeOrder(any(OrderDetailDTO.class))).thenReturn(response);

        ObjectMapper objectMapper =  new ObjectMapper();
        String stringOrderDetails = objectMapper.writeValueAsString(orderDetails);

        mockMvc.perform(post("/placeorder").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringOrderDetails))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewOrderAdmin() throws Exception {
        when(service.viewOrderAdmin(1)).thenReturn(new ResponseEntity<responseStructure<OrderDetailDTO>>(structure,HttpStatus.OK));

        mockMvc.perform(get("/admin/vieworder/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewOrder() throws Exception {
        when(service.viewOrder(1)).thenReturn(new ResponseEntity<responseStructure<OrderDetailDTO>>(structure,HttpStatus.OK));

        mockMvc.perform(get("/vieworder/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewAllOrdersAdmin() throws Exception {
        when(service.viewAllOrdersAdmin()).thenReturn(Listresponse);

        mockMvc.perform(get("/admin/viewallorders"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void viewAllOrders() throws Exception {
        when(service.viewAllOrders()).thenReturn(Listresponse);

        mockMvc.perform(get("/viewallorders"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void updateOrder() throws Exception {
        when(service.updateOrderAdmin(any(OrderDetailDTO.class),eq(1))).thenReturn(response);

        ObjectMapper objectMapper =  new ObjectMapper();
        String stringOrderDetails = objectMapper.writeValueAsString(orderDetails);

        mockMvc.perform(put("/admin/updateorder/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringOrderDetails))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void testUpdateOrder() throws Exception {
        when(service.updateOrderUser(eq(1),any(OrderDetailDTO.class))).thenReturn(response);

        ObjectMapper objectMapper =  new ObjectMapper();
        String stringOrderDetails = objectMapper.writeValueAsString(orderDetails);

        mockMvc.perform(put("/updateorder/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringOrderDetails))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void deleteOrderAdmin() throws Exception {
        when(service.deleteOrderAdmin(1)).thenReturn(response);

        mockMvc.perform(delete("/admin/deleteorder/1").with(csrf()))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void deleteOrder() throws Exception {
        when(service.deleteOrder(1)).thenReturn(response);

        mockMvc.perform(delete("/deleteorder/1").with(csrf()))
                .andExpect(status().isAccepted());
    }
}