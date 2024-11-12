package com.project.BookStore.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.BookStore.AuthenticatedUser.AuthenticatedUserDetails;
import com.project.BookStore.DTO.OrderDetailDTO;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.exception.BookNotFoundException;
import com.project.BookStore.exception.CustomerNotFoundException;
import com.project.BookStore.exception.InvalidRequestException;
import com.project.BookStore.exception.OrderNotFoundException;
import com.project.BookStore.model.book;
import com.project.BookStore.model.customer;
import com.project.BookStore.model.orderDetails;
import com.project.BookStore.repository.bookRepo;
import com.project.BookStore.repository.customerRepo;
import com.project.BookStore.repository.orderDetailsRepo;
import com.project.BookStore.repository.userCredentialsRepo;


@Service
public class orderDetailService {

	@Autowired
	private orderDetailsRepo repo;
	
	@Autowired
	private customerRepo customerRepo;
	
	@Autowired
	private bookRepo bookRepo;
	
	@Autowired
	private userCredentialsRepo credentials;
	
	public OrderDetailDTO OrderDTOConverter(orderDetails Order) {
		OrderDetailDTO DTO = new OrderDetailDTO();
		DTO.setCustomerId(Order.getCustomer().getCustomerId());
		DTO.setOrderId(Order.getOrderId());
		DTO.setName(Order.getCustomer().getName());
		DTO.setPrice(Order.getBook().getPrice()*Order.getQuantity());
		DTO.setQuantity(Order.getQuantity());
		DTO.setTitle(Order.getBook().getTitle());
		DTO.setBookId(Order.getBook().getBookId());
		return DTO;
	}
	
	public List<OrderDetailDTO> convertToOrderDetailDTOList(List<orderDetails> orders) {
	    return orders.stream()
	                 .map(this::OrderDTOConverter)
	                 .collect(Collectors.toList());
	}
	
	public ResponseEntity<responseStructure<OrderDetailDTO>> placeOrderAdmin(OrderDetailDTO order) {
		responseStructure<OrderDetailDTO> structure = new responseStructure<OrderDetailDTO>();
		if(repo.findById(order.getOrderId()).isPresent()) throw new InvalidRequestException("Order with The Id "+order.getOrderId()+" Already Exists");
		Optional<customer> OptionalCustomer = customerRepo.findById(order.getCustomerId());
		Optional<book> OptionalBook = bookRepo.findById(order.getBookId());
		
		
		if(OptionalBook.isEmpty()) throw new BookNotFoundException("Unable To Place Order With Book Id "+order.getBookId()+" Cause Book With Specified Id Not Found");
		if(OptionalCustomer.isEmpty()) throw new CustomerNotFoundException("Unable To Place Order With Customer Id "+order.getCustomerId()+" Cause Customer With Specified Id Not Found");
		
		
		if(OptionalBook.get().getQuantity()<order.getQuantity()) {
			throw new BookNotFoundException("Book With Specified Quantity "+order.getQuantity()+" Not Available Currently.Available Quantity Is "+OptionalBook.get().getQuantity());
		}
		OptionalBook.get().setQuantity(OptionalBook.get().getQuantity()-order.getQuantity());
		bookRepo.save(OptionalBook.get());
		
		orderDetails UpdatedOrder = new orderDetails();
		UpdatedOrder.setOrderId(order.getOrderId());
		UpdatedOrder.setQuantity(order.getQuantity());
		UpdatedOrder.setBook(OptionalBook.get());
		UpdatedOrder.setCustomer(OptionalCustomer.get());
		
		structure.setData(OrderDTOConverter(repo.save(UpdatedOrder)));
		structure.setMessage("Order Saved Successfully");
		structure.setStatus_code(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<responseStructure<OrderDetailDTO>>(structure,HttpStatus.ACCEPTED);
	}
	
	public ResponseEntity<responseStructure<OrderDetailDTO>> placeOrder(OrderDetailDTO order) {
		
		responseStructure<OrderDetailDTO> structure = new responseStructure<OrderDetailDTO>();
		int id =credentials.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		Optional<customer> OptionalCustomer = customerRepo.findById(id);
		Optional<book> OptionalBook = bookRepo.findById(order.getBookId());
		if(OptionalBook.isEmpty()) throw new BookNotFoundException("Unable To Place Order With Book Id "+order.getBookId()+" Cause Book With Specified Id Not Found");
		if(OptionalCustomer.isEmpty()) throw new CustomerNotFoundException("Unable To Place Order With Customer Id "+id+" Cause Customer Details With Specified Id Not Found");
		orderDetails UpdatedOrder = new orderDetails();
		if(OptionalBook.get().getQuantity()<order.getQuantity()) {
			throw new BookNotFoundException("Book With Specified Quantity "+order.getQuantity()+" Not Available Currently.Available Quantity Is "+OptionalBook.get().getQuantity());
		}
		OptionalBook.get().setQuantity(OptionalBook.get().getQuantity()-order.getQuantity());
		bookRepo.save(OptionalBook.get());
		UpdatedOrder.setOrderId(order.getOrderId());
		UpdatedOrder.setQuantity(order.getQuantity());
		UpdatedOrder.setBook(OptionalBook.get());
		UpdatedOrder.setCustomer(OptionalCustomer.get());
		structure.setData(OrderDTOConverter(repo.save(UpdatedOrder)));
		structure.setMessage("Order Saved Successfully");
		structure.setStatus_code(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<responseStructure<OrderDetailDTO>>(structure,HttpStatus.ACCEPTED);
	}
	
	
	public ResponseEntity<responseStructure<OrderDetailDTO>> viewOrderAdmin(int id) {
		responseStructure<OrderDetailDTO> structure = new responseStructure<OrderDetailDTO>();
		Optional<orderDetails> OptionalOrder = repo.findById(id);
		if(OptionalOrder.isPresent()) {
			structure.setData(OrderDTOConverter(OptionalOrder.get()));
			structure.setMessage("Order Details Found");
			structure.setStatus_code(HttpStatus.OK.value());
			return new ResponseEntity<responseStructure<OrderDetailDTO>>(structure,HttpStatus.OK);
		}throw new OrderNotFoundException("Order With Id "+id+" Not Found");
	}
	
	public ResponseEntity<responseStructure<OrderDetailDTO>> viewOrder(int id) {
		responseStructure<OrderDetailDTO> structure = new responseStructure<OrderDetailDTO>();
		int Id =credentials.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		Optional<customer> customer = customerRepo.findById(Id);
		if(customer.isPresent()) {
		Optional<orderDetails> OptionalOrder = repo.findById(id);
		if(OptionalOrder.isPresent()){
		if(customer.get()==OptionalOrder.get().getCustomer()) {
			structure.setData(OrderDTOConverter(OptionalOrder.get()));
			structure.setMessage("Order Details Found");
			structure.setStatus_code(HttpStatus.OK.value());
			return new ResponseEntity<responseStructure<OrderDetailDTO>>(structure,HttpStatus.OK);
		}throw new OrderNotFoundException("Order With Id "+id+" Not Found");
		}throw new OrderNotFoundException("Order With Id "+id+" Not Found");
		}throw new CustomerNotFoundException("No Orders Found.Unregistered Customer");
	}
	
	
	public ResponseEntity<responseStructure<List<OrderDetailDTO>>> viewAllOrdersAdmin(){
		responseStructure<List<OrderDetailDTO>> structure = new responseStructure<List<OrderDetailDTO>>();
		List<orderDetails> Orders =  repo.findAll();
		if(Orders.isEmpty()) throw new OrderNotFoundException("No Order Found");
		structure.setData(convertToOrderDetailDTOList(Orders));
		structure.setMessage("Orders Found");
		structure.setStatus_code(HttpStatus.OK.value());
		return new ResponseEntity<responseStructure<List<OrderDetailDTO>>>(structure,HttpStatus.OK);
	}
	
	public ResponseEntity<responseStructure<List<OrderDetailDTO>>> viewAllOrders(){
		responseStructure<List<OrderDetailDTO>> structure = new responseStructure<List<OrderDetailDTO>>();
		int CurrentUserId =credentials.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		Optional<customer> customer = customerRepo.findById(CurrentUserId);
		if(customer.isEmpty()) throw new CustomerNotFoundException("Unregistered Customer.No Customer and Order Details Found");
		List<orderDetails> Orders = repo.findByCustomer(customer.get()).get();
		if(Orders.isEmpty()) throw new OrderNotFoundException("No Order Found");
		structure.setData(convertToOrderDetailDTOList(Orders));
		structure.setMessage("Orders Found");
		structure.setStatus_code(HttpStatus.OK.value());
		return new ResponseEntity<responseStructure<List<OrderDetailDTO>>>(structure,HttpStatus.OK);
	}
	
	public ResponseEntity<responseStructure<OrderDetailDTO>> updateOrderAdmin(OrderDetailDTO order,int Id) {
		responseStructure<OrderDetailDTO> structure = new responseStructure<OrderDetailDTO>();
		Optional<orderDetails> OptionalOrder = repo.findById(Id);
		if(OptionalOrder.isPresent()) {
			orderDetails Order = OptionalOrder.get();
			if(order.getCustomerId()==0) order.setCustomerId(Order.getCustomer().getCustomerId());
			if(order.getBookId()==0) order.setBookId(Order.getBook().getBookId());
	        if(order.getQuantity()==0) order.setQuantity(Order.getQuantity());
			Optional<customer> OptionalCustomer = customerRepo.findById(order.getCustomerId());
			if (OptionalCustomer.isEmpty()) throw new CustomerNotFoundException("Unable To Update.Cause Customer With Id "+order.getCustomerId()+" Not Found");
			Optional<book> OptionalBook = bookRepo.findById(order.getBookId());
			
			if (OptionalBook.isEmpty()) throw new CustomerNotFoundException("Unable To Update.Cause Book With Id "+order.getBookId()+" Not Found");
			if((OptionalBook.get().getQuantity()+OptionalOrder.get().getQuantity())<order.getQuantity()) {
				throw new BookNotFoundException("Book With Specified Quantity "+order.getQuantity()+" Not Available Currently.Available Quantity Is "+(OptionalBook.get().getQuantity()+OptionalOrder.get().getQuantity()));
			}
			if(order.getQuantity()>OptionalOrder.get().getQuantity()) OptionalBook.get().setQuantity((OptionalBook.get().getQuantity()+OptionalOrder.get().getQuantity())-(order.getQuantity()));
			if(order.getQuantity()<OptionalOrder.get().getQuantity()) OptionalBook.get().setQuantity(OptionalBook.get().getQuantity()+(OptionalOrder.get().getQuantity()-order.getQuantity()));
			Order.setOrderId(Id);
			Order.setQuantity(order.getQuantity());
		    Order.setBook(OptionalBook.get());
			Order.setCustomer(OptionalCustomer.get());
			structure.setData(OrderDTOConverter(repo.save(Order)));
			structure.setMessage("Updated Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<OrderDetailDTO>>(structure,HttpStatus.ACCEPTED);
		}throw new OrderNotFoundException("Order With Id "+Id+" Not Found.Unable To Upadte");
		
	}
	
	public ResponseEntity<responseStructure<OrderDetailDTO>> updateOrderUser(int Id,OrderDetailDTO order) {
		responseStructure<OrderDetailDTO> structure = new responseStructure<OrderDetailDTO>();
		Optional<orderDetails> OptionalOrder = repo.findById(Id);
		int CurrentUserId =credentials.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		if(OptionalOrder.isPresent()) {
	        orderDetails Order = OptionalOrder.get();
	        if(order.getBookId()==0) order.setBookId(OptionalOrder.get().getBook().getBookId());
	        if(order.getQuantity()==0) order.setQuantity(OptionalOrder.get().getQuantity());
			if(Order.getCustomer().getCustomerId()!=CurrentUserId) throw new OrderNotFoundException("You Have No Order With OrderId "+Id);
			Optional<book> OptionalBook = bookRepo.findById(order.getBookId());
			if (OptionalBook.isEmpty()) throw new BookNotFoundException("Unable To Update.Cause Book With Id "+order.getBookId()+" Not Found");
			if((OptionalBook.get().getQuantity()+OptionalOrder.get().getQuantity())<order.getQuantity()) {
				throw new BookNotFoundException("Book With Specified Quantity "+order.getQuantity()+" Not Available Currently.Available Quantity Is "+(OptionalBook.get().getQuantity()+OptionalOrder.get().getQuantity()));
			}
			if(order.getQuantity()>OptionalOrder.get().getQuantity()) OptionalBook.get().setQuantity((OptionalBook.get().getQuantity()+OptionalOrder.get().getQuantity())-(order.getQuantity()));
			if(order.getQuantity()<OptionalOrder.get().getQuantity()) OptionalBook.get().setQuantity(OptionalBook.get().getQuantity()+(OptionalOrder.get().getQuantity()-order.getQuantity()));
			bookRepo.save(OptionalBook.get());

			Optional<customer> customer = customerRepo.findById(CurrentUserId);
			if (customer.isEmpty()) throw new CustomerNotFoundException("UnRegistered Customer.No Customer Details Found");

			Order.setOrderId(Id);
			Order.setQuantity(order.getQuantity());
		    Order.setBook(OptionalBook.get());
			Order.setCustomer(customer.get());
			structure.setData(OrderDTOConverter(repo.save(Order)));
			structure.setMessage("Updated Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<OrderDetailDTO>>(structure,HttpStatus.ACCEPTED);
		}throw new OrderNotFoundException("Order With Id "+Id+" Not Found.Unable To Upadte");
		
	}
	
	
	public ResponseEntity<responseStructure<OrderDetailDTO>> deleteOrderAdmin(int id){
		responseStructure<OrderDetailDTO> structure = new responseStructure<OrderDetailDTO>();
		Optional<orderDetails> OptionalOrder = repo.findById(id);
		if (OptionalOrder.isPresent()) {
			repo.delete(OptionalOrder.get());
			structure.setData(OrderDTOConverter(OptionalOrder.get()));
			structure.setMessage("Deleted Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<OrderDetailDTO>>(structure,HttpStatus.ACCEPTED);
		}throw new OrderNotFoundException("Order With Id "+id+" Not Found.Unable To Delete");
	}
	
	public ResponseEntity<responseStructure<OrderDetailDTO>> deleteOrder(int Id){
		responseStructure<OrderDetailDTO> structure = new responseStructure<OrderDetailDTO>();
		Optional<orderDetails> OptionalOrder = repo.findById(Id);
		int CurrentUserId =credentials.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		if (OptionalOrder.isPresent()) {
			if(OptionalOrder.get().getCustomer().getCustomerId()!=CurrentUserId) throw new OrderNotFoundException("You Have No Order With OrderId "+Id);
			repo.delete(OptionalOrder.get());
			structure.setData(OrderDTOConverter(OptionalOrder.get()));
			structure.setMessage("Deleted Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<OrderDetailDTO>>(structure,HttpStatus.ACCEPTED);
		}throw new OrderNotFoundException("Order With Id "+Id+" Not Found.Unable To Delete");
	}
	
}
