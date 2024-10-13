package com.project.BookStore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.BookStore.AuthenticatedUser.AuthenticatedUserDetails;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.exception.BookNotFoundException;
import com.project.BookStore.exception.CustomerNotFoundException;
import com.project.BookStore.exception.OrderNotFoundException;
import com.project.BookStore.model.book;
import com.project.BookStore.model.customer;
import com.project.BookStore.model.orderDetails;
import com.project.BookStore.model.orderDetailsDummy;
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
	
	public ResponseEntity<responseStructure<orderDetails>> placeAnOrder(orderDetailsDummy order) {
		responseStructure<orderDetails> structure = new responseStructure<orderDetails>();
		Optional<customer> OptionalCustomer = customerRepo.findById(order.getCustomerId());
		Optional<book> OptionalBook = bookRepo.findById(order.getBookId());
		if(OptionalBook.isEmpty()) throw new BookNotFoundException("Unable To Place Order With Book Id "+order.getBookId()+" Cause Book With Specified Id Not Found");
		if(OptionalCustomer.isEmpty()) throw new CustomerNotFoundException("Unable To Place Order With Customer Id "+order.getCustomerId()+" Cause Customer With Specified Id Not Found");
		
		orderDetails UpdatedOrder = new orderDetails();
		UpdatedOrder.setOrderId(order.getOrderId());
		UpdatedOrder.setQuantity(order.getQuantity());
		UpdatedOrder.setBook(OptionalBook.get());
		UpdatedOrder.setCustomer(OptionalCustomer.get());
		structure.setData(repo.save(UpdatedOrder));
		structure.setMessage("Order Saved Successfully");
		structure.setStatus_code(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<responseStructure<orderDetails>>(structure,HttpStatus.ACCEPTED);
	}
	
	public ResponseEntity<responseStructure<orderDetails>> placeOrder(orderDetailsDummy order) {
		responseStructure<orderDetails> structure = new responseStructure<orderDetails>();
		int id =credentials.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		Optional<customer> OptionalCustomer = customerRepo.findById(id);
		Optional<book> OptionalBook = bookRepo.findById(order.getBookId());
		if(OptionalBook.isEmpty()) throw new BookNotFoundException("Unable To Place Order With Book Id "+order.getBookId()+" Cause Book With Specified Id Not Found");
		if(OptionalCustomer.isEmpty()) throw new CustomerNotFoundException("Unable To Place Order With Customer Id "+id+" Cause Customer Details With Specified Id Not Found");
		orderDetails UpdatedOrder = new orderDetails();
		UpdatedOrder.setOrderId(order.getOrderId());
		UpdatedOrder.setQuantity(order.getQuantity());
		UpdatedOrder.setBook(OptionalBook.get());
		UpdatedOrder.setCustomer(OptionalCustomer.get());
		structure.setData(repo.save(UpdatedOrder));
		structure.setMessage("Order Saved Successfully");
		structure.setStatus_code(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<responseStructure<orderDetails>>(structure,HttpStatus.ACCEPTED);
	}
	
	
	public ResponseEntity<responseStructure<orderDetails>> viewOrder(int id) {
		responseStructure<orderDetails> structure = new responseStructure<orderDetails>();
		Optional<orderDetails> OptionalOrder = repo.findById(id);
		if(OptionalOrder.isPresent()) {
			structure.setData(OptionalOrder.get());
			structure.setMessage("Order Details Found");
			structure.setStatus_code(HttpStatus.FOUND.value());
			return new ResponseEntity<responseStructure<orderDetails>>(structure,HttpStatus.FOUND);
		}throw new OrderNotFoundException("Order With Id "+id+" Not Found");
	}
	
	
	public ResponseEntity<responseStructure<List<orderDetails>>> viewAllOrdersAdmin(){
		responseStructure<List<orderDetails>> structure = new responseStructure<List<orderDetails>>();
		List<orderDetails> Orders = repo.findAll();
		if(Orders.isEmpty()) throw new BookNotFoundException("No Order Found");
		structure.setData(Orders);
		structure.setMessage("Orders Found");
		structure.setStatus_code(HttpStatus.FOUND.value());
		return new ResponseEntity<responseStructure<List<orderDetails>>>(structure,HttpStatus.FOUND);
	}
	
	
	public ResponseEntity<responseStructure<orderDetails>> updateOrderAdmin(orderDetailsDummy order,int Id) {
		responseStructure<orderDetails> structure = new responseStructure<orderDetails>();
		Optional<orderDetails> OptionalOrder = repo.findById(Id);
		if(OptionalOrder.isPresent()) {
			orderDetails Order = OptionalOrder.get();
			Optional<customer> OptionalCustomer = customerRepo.findById(order.getCustomerId());
			if (OptionalCustomer.isEmpty()) throw new CustomerNotFoundException("Unable To Update.Cause Customer With Id"+order.getCustomerId()+"Not Found");
			Optional<book> OptionalBook = bookRepo.findById(order.getBookId());
			if (OptionalBook.isEmpty()) throw new CustomerNotFoundException("Unable To Update.Cause Book With Id"+order.getBookId()+"Not Found");
			Order.setOrderId(Id);
			Order.setQuantity(order.getQuantity());
		    Order.setBook(OptionalBook.get());
			Order.setCustomer(OptionalCustomer.get());
			structure.setData(repo.save(Order));
			structure.setMessage("Updated Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<orderDetails>>(structure,HttpStatus.ACCEPTED);
		}throw new OrderNotFoundException("Order With Id "+Id+" Not Found.Unable To Upadte");
		
	}
	
	public ResponseEntity<responseStructure<orderDetails>> updateOrderUser(int Id,orderDetailsDummy order) {
		responseStructure<orderDetails> structure = new responseStructure<orderDetails>();
		Optional<orderDetails> OptionalOrder = repo.findById(Id);
		int CurrentUserId =credentials.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		if(OptionalOrder.isPresent()) {
	        orderDetails Order = OptionalOrder.get();
			if(Order.getCustomer().getCustomerId()!=CurrentUserId) throw new OrderNotFoundException("You Have No Order With OrderId "+Id);
			Optional<book> OptionalBook = bookRepo.findById(order.getBookId());
			if (OptionalBook.isEmpty()) throw new CustomerNotFoundException("Unable To Update.Cause Book With Id"+order.getBookId()+"Not Found");
			Order.setOrderId(Id);
			Order.setQuantity(order.getQuantity());
		    Order.setBook(OptionalBook.get());
			Order.setCustomer(customerRepo.findById(CurrentUserId).get());
			structure.setData(repo.save(Order));
			structure.setMessage("Updated Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<orderDetails>>(structure,HttpStatus.ACCEPTED);
		}throw new OrderNotFoundException("Order With Id "+Id+" Not Found.Unable To Upadte");
		
	}
	
	
	public ResponseEntity<responseStructure<orderDetails>> deleteOrderAdmin(int id){
		responseStructure<orderDetails> structure = new responseStructure<orderDetails>();
		Optional<orderDetails> OPtionalOrder = repo.findById(id);
		if (OPtionalOrder.isPresent()) {
			repo.delete(OPtionalOrder.get());
			structure.setData(OPtionalOrder.get());
			structure.setMessage("Deleted Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<orderDetails>>(structure,HttpStatus.ACCEPTED);
		}throw new OrderNotFoundException("Order With Id "+id+" Not Found.Unable To Delete");
	}
	
	public ResponseEntity<responseStructure<orderDetails>> deleteOrder(int Id){
		responseStructure<orderDetails> structure = new responseStructure<orderDetails>();
		Optional<orderDetails> OptionalOrder = repo.findById(Id);
		int CurrentUserId =credentials.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		if (OptionalOrder.isPresent()) {
			if(OptionalOrder.get().getCustomer().getCustomerId()!=CurrentUserId) throw new OrderNotFoundException("You Have No Order With OrderId "+Id);
			repo.delete(OptionalOrder.get());
			structure.setData(OptionalOrder.get());
			structure.setMessage("Deleted Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<orderDetails>>(structure,HttpStatus.ACCEPTED);
		}throw new OrderNotFoundException("Order With Id "+Id+" Not Found.Unable To Delete");
	}
	
	public ResponseEntity<responseStructure<List<orderDetails>>> viewAllOrders(){
		responseStructure<List<orderDetails>> structure = new responseStructure<List<orderDetails>>();
		int CurrentUserId =credentials.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		customer customer = customerRepo.findById(CurrentUserId).get();
		List<orderDetails> Orders = repo.findByCustomer(customer).get();
		if(Orders.isEmpty()) throw new OrderNotFoundException("No Order Found");
		structure.setData(Orders);
		structure.setMessage("Orders Found");
		structure.setStatus_code(HttpStatus.FOUND.value());
		return new ResponseEntity<responseStructure<List<orderDetails>>>(structure,HttpStatus.FOUND);
	}
}
