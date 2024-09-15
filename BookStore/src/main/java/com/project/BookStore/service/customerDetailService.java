package com.project.BookStore.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.project.BookStore.model.customer;
import com.project.BookStore.repository.customerRepo;



@Service
public class customerDetailService{
	
	@Autowired
	private customerRepo repo;
	
	public customer addCustomer(customer customer){
		return repo.save(customer);
	}
	
	
	public List<customer> viewAllCustomers() {
		return repo.findAll();
	}
	
	
	public customer viewCustomerById(int id) {
		return repo.findById(id).orElseThrow();
	}
	
	
	public List<customer> viewCustomerByName(String name) {
		return repo.findByName(name);
	}
	
	
	public customer updateCustomerDetail(customer customer,int id) {
		customer Customer = repo.findById(id).orElseThrow();
		Customer.setCustomerId(id);
		Customer.setName(customer.getName());
		Customer.setEmail(customer.getEmail());
		return repo.save(Customer);
	}
	
	
	public customer deleteCustomerById(int id){
		customer customer = repo.findById(id).orElseThrow();
		repo.deleteById(id);
		return customer;
	}
	
	
	public List<customer> deleteCustomerByName(String name) {
		List<customer> Customer = repo.findByName(name);
		repo.deleteAllInBatch(Customer);
		return Customer;
	}
	
}
