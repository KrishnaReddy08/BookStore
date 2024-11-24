package com.project.BookStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BookStore.DTO.CustomerDetailDTO;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.JWT.jwtservice;
import com.project.BookStore.model.customer;
import com.project.BookStore.repository.customerRepo;
import com.project.BookStore.service.UserService;

import com.project.BookStore.service.customerDetailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(customerDetailController.class)
@AutoConfigureMockMvc
class customerDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private customerDetailService service;
    @MockBean
    private customerRepo repo;

    @MockBean
    private jwtservice jwtservice;

    @MockBean
    private UserService userService;

    private responseStructure<CustomerDetailDTO> structure;
    private ResponseEntity<responseStructure<CustomerDetailDTO>> response;

    private responseStructure<List<CustomerDetailDTO>> Liststructure;
    private ResponseEntity<responseStructure<List<CustomerDetailDTO>>> Listresponse;

    private  customer customer;
    private List<customer> customers = new ArrayList<>();


    @BeforeEach
    void setUp() {
        customer = new customer();

        customers.add(customer);

        structure = new responseStructure<>();
        response = new ResponseEntity<responseStructure<CustomerDetailDTO>>(structure, HttpStatus.ACCEPTED);

        Liststructure = new responseStructure<>();
        Listresponse = new ResponseEntity<responseStructure<List<CustomerDetailDTO>>>(Liststructure,HttpStatus.OK);

    }

    @AfterEach
    void tearDown() {
        structure = null;
        response = null;
        Listresponse = null;
        Liststructure = null;
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void addCustomer() throws Exception {
        when(service.addCustomer(any(customer.class))).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(post("/admin/addcustomer").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(status().isAccepted());

    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void viewAllCustomers() throws Exception {
        when(service.viewAllCustomers()).thenReturn(Listresponse);
        mockMvc.perform(get("/admin/viewallcustomers").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void viewById() throws Exception {
        when(service.viewCustomerById(1)).thenReturn(new ResponseEntity<responseStructure<CustomerDetailDTO>>(structure,HttpStatus.OK));

        mockMvc.perform(get("/admin/viewcustomer/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void viewCustomer() throws Exception {
        when(service.viewCurrentCustomer()).thenReturn(new ResponseEntity<responseStructure<CustomerDetailDTO>>(structure,HttpStatus.OK));

        mockMvc.perform(get("/viewcurrentcustomer").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void viewByName() throws Exception {
        when(service.viewCustomerByName("name")).thenReturn(Listresponse);

        mockMvc.perform(get("/admin/viewcustomerbyname/name").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void upadteCustomer() throws Exception {
        when(service.updateCustomerDetailAdmin(any(customer.class),eq(1))).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(put("/admin/updatecustomer/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void testUpadteCustomer() throws Exception {
        when(service.updateCustomerDetail(any(customer.class))).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(put("/updatecurrentcustomer").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void deleteCustomerById() throws Exception {
        when(service.deleteCustomerById(1)).thenReturn(response);

        mockMvc.perform(delete("/admin/deletecustomer/1").with(csrf()))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void deleteCurrentCustomer() throws Exception {
            when(service.deleteCurrentCustomer()).thenReturn(response);

            mockMvc.perform(delete("/deletecurrentcustomer").with(csrf()))
                    .andExpect(status().isAccepted());
    }

}