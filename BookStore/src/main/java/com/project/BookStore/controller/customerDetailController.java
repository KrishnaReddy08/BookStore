package com.project.BookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.BookStore.DTO.CustomerDetailDTO;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.model.customer;
import com.project.BookStore.service.customerDetailService;


@RestController
public class customerDetailController {
	
	@Autowired
	private customerDetailService service;
	
	@PostMapping("/admin/addcustomer")
	public  ResponseEntity<responseStructure<CustomerDetailDTO>> addCustomer(@RequestBody customer customer) {
		System.out.println("in controller"+customer);
		return service.addCustomer(customer);
	}
	
	
	@GetMapping("/admin/viewallcustomers")
	public ResponseEntity<responseStructure<List<CustomerDetailDTO>>> viewAllCustomers() {
		return service.viewAllCustomers();
	}
	
	
	@GetMapping("/admin/viewcustomer/{id}")
	public ResponseEntity<responseStructure<CustomerDetailDTO>> viewById(@PathVariable int id) {
			return service.viewCustomerById(id);
	}
	
	@GetMapping("/viewcurrentcustomer")
	public ResponseEntity<responseStructure<CustomerDetailDTO>> viewCustomer() {
			return service.viewCurrentCustomer();
	}
	
	@GetMapping("/admin/viewcustomerbyname/{name}")
	public  ResponseEntity<responseStructure<List<CustomerDetailDTO>>> viewByName(@PathVariable String name) {
		return service.viewCustomerByName(name);
	}
	
	
	@PutMapping("/admin/updatecustomer/{id}")
	public  ResponseEntity<responseStructure<CustomerDetailDTO>> upadteCustomer(@PathVariable int id,@RequestBody customer customer) {
		return service.updateCustomerDetailAdmin(customer, id);
	}
	
	@PutMapping("/updatecurrentcustomer")
	public  ResponseEntity<responseStructure<CustomerDetailDTO>> upadteCustomer(@RequestBody customer customer) {
		return service.updateCustomerDetail(customer);
	}
	
	
	@DeleteMapping("/admin/deletecustomer/{id}")
	public  ResponseEntity<responseStructure<CustomerDetailDTO>> deleteCustomerById(@PathVariable int id) {
		return service.deleteCustomerById(id);
	}
	
	@DeleteMapping("/deletecurrentcustomer")
	public  ResponseEntity<responseStructure<CustomerDetailDTO>> deleteCurrentCustomer(){
		return service.deleteCurrentCustomer();
	}
	
	@DeleteMapping("/admin/deletecustomerbyname/{name}")
	public  ResponseEntity<responseStructure<List<CustomerDetailDTO>>> deleteCustomerByname(@PathVariable String name) {
		return service.deleteCustomerByName(name);
	}
			
}
