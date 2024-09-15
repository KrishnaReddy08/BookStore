package com.project.BookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.BookStore.model.customer;
import com.project.BookStore.service.customerDetailService;


@RestController
public class customerDetailController {
	
	@Autowired
	private customerDetailService service;
	
	@PostMapping("/addcustomer")
	public ResponseEntity<?> addCustomer(@RequestBody customer customer) {
		try {
		return new ResponseEntity<>(service.addCustomer(customer),HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>("500 Internal server Error\n -> check either entered non excisting Customer Id\n -> check either entered all fields",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/viewallcustomers")
	public List<customer> viewAllCustomers() {
		return service.viewAllCustomers();
	}
	
	
	@GetMapping("/viewcustomer/{id}")
	public ResponseEntity<?> viewById(@PathVariable int id) {
		try {
		return new ResponseEntity<>(service.viewCustomerById(id),HttpStatus.FOUND);
		}
		catch(Exception e) {
			return new ResponseEntity<>("404 Customer With Specified Id Not Found",HttpStatus.NOT_FOUND);
		}
	}
	
	
	@GetMapping("/viewcustomerbyname/{name}")
	public ResponseEntity<?> viewByName(@PathVariable String name) {
		
		if(service.viewCustomerByName(name)!=null) 
			return new ResponseEntity<>(service.viewCustomerByName(name),HttpStatus.FOUND);
		
		else
			return new ResponseEntity<>("404 Customer With Specified Name Not Found",HttpStatus.NOT_FOUND);
	}
	
	
	@PutMapping("/updatecustomer/{id}")
	public ResponseEntity<?> upadteCustomer(@PathVariable int id,@RequestBody customer customer) {
		try {
			return new ResponseEntity<>(service.updateCustomerDetail(customer,id),HttpStatus.OK);
		}
		catch(DataIntegrityViolationException e) {
			return new ResponseEntity<>("Enter All Fields",HttpStatus.BAD_REQUEST);
		}
		catch(Exception e) {
			return new ResponseEntity<>("404 Customer With Specified Id Not Found",HttpStatus.NOT_FOUND);
		}	
	}
	
	
	@DeleteMapping("/deletecustomer/{id}")
	public ResponseEntity<?> deleteCustomerById(@PathVariable int id) {
		try {
			return new ResponseEntity<>(service.deleteCustomerById(id),HttpStatus.OK);
		}
		catch(DataAccessException e){
			return new ResponseEntity<>("Cannot delete a parent row",HttpStatus.BAD_REQUEST);
		}
		catch(Exception e){
			return new ResponseEntity<>("404 Not Found",HttpStatus.NOT_FOUND);
		}
	}
	
	
	@DeleteMapping("/deletecustomerbyname/{name}")
	public ResponseEntity<?> deleteCustomerByname(@PathVariable String name) {
		try {
			return new ResponseEntity<>(service.deleteCustomerByName(name),HttpStatus.OK);
		}
		catch(DataAccessException e){
			return new ResponseEntity<>("Cannot delete a parent row",HttpStatus.BAD_REQUEST);
		}
		catch(Exception e) {
			return new ResponseEntity<>("404 Not Found",HttpStatus.NOT_FOUND);
		}
	}
			
}
