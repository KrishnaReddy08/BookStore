package com.project.BookStore.service;

import com.project.BookStore.AuthenticatedUser.AuthenticatedUserDetails;
import com.project.BookStore.DTO.CustomerDetailDTO;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.exception.CustomerNotFoundException;
import com.project.BookStore.exception.InvalidPropertiesException;
import com.project.BookStore.exception.InvalidRequestException;
import com.project.BookStore.exception.UserNotFoundException;
import com.project.BookStore.model.Role;
import com.project.BookStore.model.customer;
import com.project.BookStore.model.userCredentials;
import com.project.BookStore.repository.customerRepo;
import com.project.BookStore.repository.userCredentialsRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

class customerDetailServiceTest {
    @InjectMocks
    private customerDetailService service;
    private customer customer;
    @Mock
    private customerRepo repo;

    @Mock
    private userCredentialsRepo credentialsRepo;
    private userCredentials credentials;

    private CustomerDetailDTO DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        credentials = new userCredentials();
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        credentials.setRoles(roles);
        credentials.setPassword("password");
        credentials.setUsername("user");
        credentials.setCustomerId(1);
        customer = new customer();
        customer.setName("name");
        customer.setEmail("krishna@gmail.com");
        customer.setCustomerId(1);
        credentials.setCustomer(customer);

        DTO = new CustomerDetailDTO();
        DTO.setName(customer.getName());
        DTO.setCustomerId(customer.getCustomerId());
        DTO.setEmail(customer.getEmail());
    }

    @AfterEach
    void tearDown() {
        customer= null;
        credentials=null;
        DTO=null;
    }

    @Test
    void customerDTOConvertor() {
    DTO = service.CustomerDTOConvertor(customer);
    assertEquals(customer.getName(),DTO.getName());
    assertEquals(customer.getCustomerId(),DTO.getCustomerId());
    assertEquals(customer.getCustomerId(),DTO.getCustomerId());
    }

    @Test
    public  void  TestIsValidEmail(){
        boolean validation1= service.isValidEmail("krishna@gmail.com");
        boolean validation2= service.isValidEmail("krisgmail.com");
        assertTrue(validation1);
        assertFalse(validation2);
    }

    @Test
    void convertToCustomerDetailTOList() {
        List<customer> customers = new ArrayList<>();
        customers.add(customer);
        customers.add(customer);
        List<CustomerDetailDTO> customers1= service.convertToCustomerDetailTOList(customers);
        assertEquals(customers.get(0).getName(),customers1.get(0).getName());
    }

    @Test
    void addCustomer() {
        when(credentialsRepo.findById(1)).thenReturn(Optional.of(credentials));
        when(repo.save(any(customer.class))).thenReturn(customer);
        ResponseEntity<responseStructure<CustomerDetailDTO>> response = service.addCustomer(customer);
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Added Successfully",response.getBody().getMessage());
        assertEquals(DTO.getName(),response.getBody().getData().getName());

        when(repo.existsById(customer.getCustomerId())).thenReturn(true);
        assertThrows(InvalidRequestException.class ,()->{
            service.addCustomer(customer);
        });

        when(repo.existsById(customer.getCustomerId())).thenReturn(false);
        when(credentialsRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->{
            service.addCustomer(customer);
        });

        customer.setEmail("wrongemail");
        when(credentialsRepo.findById(1)).thenReturn(Optional.of(credentials));
        assertThrows(InvalidPropertiesException.class, ()->{
            service.addCustomer(customer);
        });

    }

    @Test
    void viewAllCustomers() {
        List<customer> customers = new ArrayList<>();
        customers.add(customer);
        when(repo.findAll()).thenReturn(customers);
        ResponseEntity<responseStructure<List<CustomerDetailDTO>>> response = service.viewAllCustomers();
        assertFalse(response.getBody().getData().isEmpty());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Customers Found",response.getBody().getMessage());

        when(repo.findAll()).thenReturn(Collections.emptyList());
        assertThrows(CustomerNotFoundException.class, ()->{
            service.viewAllCustomers();
        });
    }

    @Test
    void viewCustomerById() {
        when(repo.findById(1)).thenReturn(Optional.of(customer));
        ResponseEntity<responseStructure<CustomerDetailDTO>> response = service.viewCustomerById(1);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Customer Found",response.getBody().getMessage());
        assertEquals(customer.getName(),response.getBody().getData().getName());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, ()->{
            service.viewCustomerById(1);
        });
    }

    @Test
    void viewCurrentCustomer() {
        when(credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        when(repo.findById(1)).thenReturn(Optional.of(customer));
        ResponseEntity<responseStructure<CustomerDetailDTO>> response = service.viewCurrentCustomer();
        assertEquals(HttpStatus.OK,response.getStatusCode());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, ()->{
            service.viewAllCustomers();
        });
    }
    @Test
    void viewCustomerByName() {
        List<customer> customers = new ArrayList<>();
        customers.add(customer);
        when(repo.findByName("name")).thenReturn(Optional.of(customers));
        ResponseEntity<responseStructure<List<CustomerDetailDTO>>> response = service.viewCustomerByName("name");

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Customers Found",response.getBody().getMessage());
        assertEquals(customers.get(0).getName(),response.getBody().getData().get(0).getName());

        List<customer> customers1 = new ArrayList<>();
        when(repo.findByName("name")).thenReturn(Optional.of(customers1));
        assertThrows(CustomerNotFoundException.class, ()->{
            service.viewCustomerByName("name");
        });
    }

    @Test
    void updateCustomerDetailAdmin() {
        customer updatedcustomer = customer;
        updatedcustomer.setEmail("updatedemail@gmail.com");
        when(credentialsRepo.findById(1)).thenReturn(Optional.of(credentials));
        when(repo.findById(1)).thenReturn(Optional.of(customer));
        when(repo.save(any(customer.class))).thenReturn(updatedcustomer);
        ResponseEntity<responseStructure<CustomerDetailDTO>> response = service.updateCustomerDetailAdmin(customer,1);
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Updated Successfully",response.getBody().getMessage());
        assertEquals("updatedemail@gmail.com",response.getBody().getData().getEmail());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class,()->{
            service.updateCustomerDetailAdmin(customer,1);
        });

        customer.setEmail("wrongmail");
        when(repo.findById(1)).thenReturn(Optional.of(customer));
        assertThrows(InvalidPropertiesException.class,()->{
            service.updateCustomerDetailAdmin(customer,1);
        });
    }

    @Test
    void updateCustomerDetail() {
        when(credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        when(repo.findById(1)).thenReturn(Optional.of(customer));
        when(repo.save(any(customer.class))).thenReturn(customer);
        ResponseEntity<responseStructure<CustomerDetailDTO>> response = service.updateCustomerDetail(customer);
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Updated Successfully",response.getBody().getMessage());
        assertEquals(customer.getEmail(),response.getBody().getData().getEmail());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class,()->{
            service.updateCustomerDetail(customer);
        });

        customer.setEmail("wrongmail");
        when(repo.findById(1)).thenReturn(Optional.of(customer));
        assertThrows(InvalidPropertiesException.class,()->{
            service.updateCustomerDetail(customer);
        });
    }

    @Test
    void deleteCustomerById() {
        when(repo.findById(1)).thenReturn(Optional.of(customer));
        when(credentialsRepo.findById(1)).thenReturn(Optional.of(credentials));
        ResponseEntity<responseStructure<CustomerDetailDTO>> response = service.deleteCustomerById(1);

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Deleted Successfully",response.getBody().getMessage());
        assertEquals(customer.getName(),response.getBody().getData().getName());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, ()->{
            service.deleteCustomerById(1);
        });
    }

    @Test
    void deleteCurrentCustomer() {
        when(credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        when(repo.findById(1)).thenReturn(Optional.of(customer));
        when(credentialsRepo.findById(1)).thenReturn(Optional.of(credentials));
        ResponseEntity<responseStructure<CustomerDetailDTO>> response = service.deleteCurrentCustomer();

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Deleted Successfully",response.getBody().getMessage());
        assertEquals(customer.getName(),response.getBody().getData().getName());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, ()->{
            service.deleteCurrentCustomer();
        });
    }

}