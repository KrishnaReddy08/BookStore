package com.project.BookStore.service;


import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.exception.CustomerNotFoundException;
import com.project.BookStore.model.CustomerDummy;
import com.project.BookStore.model.customer;
import com.project.BookStore.model.userCredentials;
import com.project.BookStore.repository.customerRepo;
import com.project.BookStore.repository.userCredentialsRepo;


@Service

public class customerDetailService{
	
	@Autowired
	private customerRepo repo;
	
	@Autowired
	private userCredentialsRepo credentialsRepo;
	
	public ResponseEntity<responseStructure<customer>> addCustomer(CustomerDummy customer){	
		responseStructure<customer> structure = new responseStructure<customer>();
		userCredentials UserCred = credentialsRepo.findById(customer.getCustomerId()).get();
		customer Customer = new customer();
		Customer.setCustomerId(customer.getCustomerId());
		Customer.setEmail(customer.getEmail());
		Customer.setName(customer.getName());
		
		System.out.println("user cred"+Customer.getUserCredentials());
		System.out.println("after adding usercred"+Customer);
		structure.setData(repo.save(Customer));
		structure.setMessage("Added Successfully");
		structure.setStatus_code(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<responseStructure<customer>>(structure,HttpStatus.ACCEPTED);
	}
	
	
	public ResponseEntity<responseStructure<List<customer>>> viewAllCustomers() {
		responseStructure<List<customer>> structure = new responseStructure<List<customer>>();
		List<customer> customers = repo.findAll();
		if(customers.isEmpty()) throw new CustomerNotFoundException("No Customer Found");
		structure.setData(customers);
		structure.setMessage("Customers Found");
		structure.setStatus_code(HttpStatus.FOUND.value());
		return new ResponseEntity<responseStructure<List<customer>>>(structure,HttpStatus.FOUND);	
	}
	
	
	public ResponseEntity<responseStructure<customer>> viewCustomerById(int id) {
		Optional<customer> customer = repo.findById(id);
		responseStructure<customer> structure = new responseStructure<customer>();
		if(customer.isPresent()) {
			structure.setData(customer.get());
			structure.setMessage("Customer Found");
			structure.setStatus_code(HttpStatus.FOUND.value());
			return new ResponseEntity<responseStructure<customer>>(structure,HttpStatus.OK);
		}
		throw new CustomerNotFoundException("Customer With Id "+id+" Not Found");
	}
	
	
	public ResponseEntity<responseStructure<List<customer>>> viewCustomerByName(String name) {
		List<customer> customer = repo.findByName(name).get();
		responseStructure<List<customer>> structure = new responseStructure<List<customer>>();
		if(customer.isEmpty()) {
			throw new CustomerNotFoundException("Customer With Name "+name+" Not Found");
		}
		structure.setData(customer);
		structure.setMessage("Customers Found");
		structure.setStatus_code(HttpStatus.FOUND.value());
		return new ResponseEntity<responseStructure<List<customer>>>(structure,HttpStatus.OK);
	}
	
	
	public ResponseEntity<responseStructure<customer>> updateCustomerDetail(customer customer,int id) {
		responseStructure<customer> structure = new responseStructure<customer>();
		Optional<customer> OptionalCustomer = repo.findById(id);
		if(OptionalCustomer.isPresent()) {
			customer Customer = OptionalCustomer.get();
			Customer.setName(customer.getName());
			Customer.setEmail(customer.getEmail());
			structure.setData(repo.save(Customer));
			structure.setMessage("Updated Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<customer>>(structure,HttpStatus.ACCEPTED);
		}
		throw new CustomerNotFoundException("Customer With Id "+id+" Not Found");
	}
	
	
	public ResponseEntity<responseStructure<customer>> deleteCustomerById(int id){
		Optional<customer> OptionalCustomer = repo.findById(id);
		responseStructure<customer> structure = new responseStructure<customer>();
		if(OptionalCustomer.isPresent()) {
			customer Customer = OptionalCustomer.get();
			repo.deleteById(id);
			structure.setData(Customer);
			structure.setMessage("Deleted Sucessfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<customer>>(structure,HttpStatus.ACCEPTED);
		}
		throw new CustomerNotFoundException("Customer With Id "+id+" Not Found.Unable To Delete");
	}
	
	
	public ResponseEntity<responseStructure<List<customer>>> deleteCustomerByName(String name) {
		Optional<List<customer>> OptionalCustomer = repo.findByName(name);
		responseStructure<List<customer>> structure = new responseStructure<List<customer>>();
		if(OptionalCustomer.isPresent()) {
			List<customer> customer = OptionalCustomer.get();
			repo.deleteAllInBatch(customer);
			structure.setData(customer);
			structure.setMessage("Deleted Sucessfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<List<customer>>>(structure,HttpStatus.ACCEPTED);
		}
		throw new CustomerNotFoundException("Customer With Name "+name+" Not Found.Unable To Delete");
	}
}
