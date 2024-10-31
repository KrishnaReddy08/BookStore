package com.project.BookStore.service;

import com.project.BookStore.AuthenticatedUser.AuthenticatedUserDetails;
import com.project.BookStore.DTO.OrderDetailDTO;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.exception.BookNotFoundException;
import com.project.BookStore.exception.CustomerNotFoundException;
import com.project.BookStore.exception.OrderNotFoundException;
import com.project.BookStore.model.book;
import com.project.BookStore.model.customer;
import com.project.BookStore.model.orderDetails;
import com.project.BookStore.model.userCredentials;
import com.project.BookStore.repository.bookRepo;
import com.project.BookStore.repository.customerRepo;
import com.project.BookStore.repository.orderDetailsRepo;
import com.project.BookStore.repository.userCredentialsRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

class orderDetailServiceTest {

    @InjectMocks
    private orderDetailService service;

    private orderDetails order1;
    @Mock
    private orderDetailsRepo repo;

    @Mock
    private customerRepo customerRepo;
    private customer customer;

    @Mock
    private bookRepo bookRepo;
    private book book;

    @Mock
    private userCredentialsRepo credentialsRepo;
    private userCredentials credentials;

    private OrderDetailDTO order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        order = new OrderDetailDTO();
        order.setOrderId(1);
        order.setBookId(1);
        order.setQuantity(4);
        order.setCustomerId(1);

        credentials = new  userCredentials();
        credentials.setCustomerId(1);

        customer = new customer();
        customer.setCustomerId(1);

        book = new book();
        book.setBookId(1);
        book.setQuantity(4);
        book.setPrice(23F);

        order1 = new orderDetails();
        order1.setOrderId(1);
        order1.setQuantity(4);
        order1.setBook(book);
        order1.setCustomer(customer);
    }

    @AfterEach
    void tearDown() {
        order=null;
        order1=null;
        credentials=null;
        customer=null;
        book=null;
        customerRepo.deleteAll();
        customerRepo.deleteAll();
        bookRepo.deleteAll();
        repo.deleteAll();
    }

    @Test
    void orderDTOConverter() {
        OrderDetailDTO DTO = service.OrderDTOConverter(order1);
        assertEquals(order1.getOrderId(),DTO.getOrderId());
        assertEquals(order1.getCustomer().getName(),DTO.getName());
        assertEquals(order1.getCustomer().getCustomerId(),DTO.getCustomerId());
        assertEquals(order1.getQuantity(),DTO.getQuantity());
        assertEquals(order1.getBook().getPrice()*order1.getQuantity(),DTO.getPrice());
        assertEquals(order1.getBook().getBookId(),DTO.getBookId());
        assertEquals(order1.getBook().getTitle(),DTO.getTitle());
    }

    @Test
    void convertToOrderDetailDTOList() {
        List<orderDetails> orders = new ArrayList<>();
        orders.add(order1);
        List<OrderDetailDTO> orderDto = service.convertToOrderDetailDTOList(orders);
        assertEquals(orders.get(0).getQuantity(),orderDto.get(0).getQuantity());
    }

    @Test
    void placeOrderAdmin() {
        when(customerRepo.findById(order.getCustomerId())).thenReturn(Optional.of(customer));
        when(bookRepo.findById(order.getBookId())).thenReturn(Optional.of(book));
        when(repo.save(any(orderDetails.class))).thenReturn(order1);
        ResponseEntity<responseStructure<OrderDetailDTO>> response = service.placeOrderAdmin(order);
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Order Saved Successfully",response.getBody().getMessage());
        assertEquals(order.getQuantity(),response.getBody().getData().getQuantity());

        order.setQuantity(5);
        assertThrows(BookNotFoundException.class, ()->{
            service.placeOrderAdmin(order);
        });

        order.setQuantity(4);
        when(bookRepo.findById(order.getBookId())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, ()->{
            service.placeOrderAdmin(order);
        });

        when(bookRepo.findById(order.getBookId())).thenReturn(Optional.of(book));
        when(customerRepo.findById(order.getCustomerId())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, ()->{
            service.placeOrderAdmin(order);
        });


    }

    @Test
    void placeOrder() {
        when(credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        when(customerRepo.findById(order.getCustomerId())).thenReturn(Optional.of(customer));
        when(bookRepo.findById(order.getBookId())).thenReturn(Optional.of(book));
        when(repo.save(any(orderDetails.class))).thenReturn(order1);
        ResponseEntity<responseStructure<OrderDetailDTO>> response = service.placeOrder(order);
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Order Saved Successfully",response.getBody().getMessage());
        assertEquals(order.getQuantity(),response.getBody().getData().getQuantity());

        order.setQuantity(5);
        assertThrows(BookNotFoundException.class, ()->{
            service.placeOrder(order);
        });


        order.setQuantity(1);
        when(bookRepo.findById(order.getBookId())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, ()->{
            service.placeOrderAdmin(order);
        });

        when(bookRepo.findById(order.getBookId())).thenReturn(Optional.of(book));
        when(customerRepo.findById(order.getCustomerId())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, ()->{
            service.placeOrderAdmin(order);
        });
    }

    @Test
    void viewOrderAdmin() {
        when(repo.findById(1)).thenReturn(Optional.of(order1));
        ResponseEntity<responseStructure<OrderDetailDTO>> response = service.viewOrderAdmin(1);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Order Details Found",response.getBody().getMessage());
        assertEquals(order.getQuantity()*order1.getBook().getPrice(),response.getBody().getData().getPrice());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,()->{
            service.viewOrderAdmin(1);
        });
    }

    @Test
    void viewOrder() {
        when(credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        when(repo.findById(1)).thenReturn(Optional.of(order1));
        when(customerRepo.findById(1)).thenReturn(Optional.of(customer));
        ResponseEntity<responseStructure<OrderDetailDTO>> response = service.viewOrder(1);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Order Details Found",response.getBody().getMessage());
        assertEquals(order.getQuantity()*order1.getBook().getPrice(),response.getBody().getData().getPrice());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,()->{
            service.viewOrder(1);
        });

        when(bookRepo.findById(order.getBookId())).thenReturn(Optional.of(book));
        when(customerRepo.findById(order.getCustomerId())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, ()->{
            service.placeOrderAdmin(order);
        });
    }

    @Test
    void viewAllOrdersAdmin() {
        List<orderDetails> orders = new ArrayList<>();
        orders.add(order1);
        when(repo.findAll()).thenReturn(orders);
        ResponseEntity<responseStructure<List<OrderDetailDTO>>> response = service.viewAllOrdersAdmin();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Orders Found",response.getBody().getMessage());
        assertEquals(orders.get(0).getQuantity(),response.getBody().getData().get(0).getQuantity());

        when(repo.findAll()).thenReturn(Collections.emptyList());
        assertThrows(OrderNotFoundException.class,()->{
            service.viewAllOrdersAdmin();
        });
    }

    @Test
    void viewAllOrders() {
        List<orderDetails> orders = new ArrayList<>();
        orders.add(order1);
        when(credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        when(customerRepo.findById(1)).thenReturn(Optional.of(customer));
        when(repo.findByCustomer(customer)).thenReturn(Optional.of(orders));

        ResponseEntity<responseStructure<List<OrderDetailDTO>>> response = service.viewAllOrders();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Orders Found",response.getBody().getMessage());
        assertEquals(orders.get(0).getQuantity(),response.getBody().getData().get(0).getQuantity());

        when(repo.findByCustomer(customer)).thenReturn(Optional.of(Collections.emptyList()));
        assertThrows(OrderNotFoundException.class,()->{
            service.viewAllOrders();
        });

        when(customerRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, ()->{
            service.viewAllOrders();
        });

    }

    @Test
    void updateOrderAdmin() {
        when(repo.findById(1)).thenReturn(Optional.of(order1));
        when(bookRepo.findById(order.getCustomerId())).thenReturn(Optional.of(book));
        when(customerRepo.findById(order.getBookId())).thenReturn(Optional.of(customer));
        when(repo.save(any(orderDetails.class))).thenReturn(order1);
        ResponseEntity<responseStructure<OrderDetailDTO>> response = service.updateOrderAdmin(order,1);

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Updated Successfully",response.getBody().getMessage());
        assertEquals(order.getQuantity(),response.getBody().getData().getQuantity());

        order.setQuantity(10);
        assertThrows(BookNotFoundException.class, ()->{
            service.updateOrderAdmin(order,1);
        });

        order.setQuantity(4);
        when(bookRepo.findById(order.getBookId())).thenReturn(Optional.of(book));
        when(customerRepo.findById(order.getCustomerId())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, ()->{
            service.updateOrderAdmin(order,1);
        });

        when(repo.findById(order.getOrderId())).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,()->{
            service.updateOrderAdmin(order,1);
        });
    }

    @Test
    void updateOrderUser() {
        when(credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        when(repo.findById(1)).thenReturn(Optional.of(order1));
        when(bookRepo.findById(order.getCustomerId())).thenReturn(Optional.of(book));
        when(customerRepo.findById(order.getBookId())).thenReturn(Optional.of(customer));
        when(repo.save(any(orderDetails.class))).thenReturn(order1);
        ResponseEntity<responseStructure<OrderDetailDTO>> response = service.updateOrderUser(1,order);

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Updated Successfully",response.getBody().getMessage());
        assertEquals(order.getQuantity(),response.getBody().getData().getQuantity());

        order.setQuantity(10);
        assertThrows(BookNotFoundException.class, ()->{
            service.updateOrderUser(1,order);
        });


        order.setQuantity(4);
        when(customerRepo.findById(order.getCustomerId())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, ()->{
            service.updateOrderUser(1,order);
        });

        when(repo.findById(order.getOrderId())).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,()->{
            service.updateOrderAdmin(order,1);
        });
    }

    @Test
    void deleteOrderAdmin() {
        when(repo.findById(1)).thenReturn(Optional.of(order1));
        ResponseEntity<responseStructure<OrderDetailDTO>> response = service.deleteOrderAdmin(1);

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Deleted Successfully",response.getBody().getMessage());
        assertEquals(order1.getQuantity(),response.getBody().getData().getQuantity());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,()->{
            service.deleteOrderAdmin(1);
        });
    }

    @Test
    void deleteOrder() {
        when(credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        when(repo.findById(1)).thenReturn(Optional.of(order1));
        ResponseEntity<responseStructure<OrderDetailDTO>> response = service.deleteOrder(1);

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals("Deleted Successfully",response.getBody().getMessage());
        assertEquals(order1.getQuantity(),response.getBody().getData().getQuantity());

        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,()->{
            service.deleteOrder(1);
        });

        credentials.setCustomerId(2);
        when(repo.findById(1)).thenReturn(Optional.of(order1));
        when(credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser())).thenReturn(Optional.of(credentials));
        assertThrows(OrderNotFoundException.class,()->{
            service.deleteOrder(1);
        });
    }
}