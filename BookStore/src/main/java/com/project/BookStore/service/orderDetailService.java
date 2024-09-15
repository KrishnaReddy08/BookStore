package com.project.BookStore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.project.BookStore.model.book;
import com.project.BookStore.model.customer;
import com.project.BookStore.model.orderDetails;
import com.project.BookStore.model.orderDetailsDummy;
import com.project.BookStore.repository.bookRepo;
import com.project.BookStore.repository.customerRepo;
import com.project.BookStore.repository.orderDetailsRepo;


@Service
public class orderDetailService {
	
	@Autowired
	private orderDetailsRepo repo;
	
	@Autowired
	private customerRepo customerRepo;
	
	@Autowired
	private bookRepo bookRepo;
	
	public orderDetails placeAnOrder(orderDetailsDummy order) {
		customer customer = customerRepo.findById(order.getCustomerId()).orElseThrow();
		book book = bookRepo.findById(order.getBookId()).orElseThrow();
		orderDetails UpdatedOrder = new orderDetails();
		UpdatedOrder.setOrderId(order.getOrderId());
		UpdatedOrder.setQuantity(order.getQuantity());
		UpdatedOrder.setBook(book);
		UpdatedOrder.setCustomer(customer);
		return repo.save(UpdatedOrder);
	}
	
	
	public orderDetails viewOrder(int id) {
		return repo.findById(id).orElseThrow();
	}
	
	
	public List<orderDetails> viewAllOrders(){
		return repo.findAll();
	}
	
	
	public orderDetails updateOrder(orderDetailsDummy order,int Id) {
		orderDetails updatedOrder=repo.findById(Id).orElseThrow();
		customer customer = customerRepo.findById(order.getCustomerId()).orElseThrow();
		book book = bookRepo.findById(order.getBookId()).orElseThrow();
		updatedOrder.setOrderId(Id);
		updatedOrder.setQuantity(order.getQuantity());
		updatedOrder.setBook(book);
		updatedOrder.setCustomer(customer);
		return repo.save(updatedOrder);
	}
	
	
	public orderDetails deleteOrder(int id){
		orderDetails order;
		order = repo.findById(id).orElseThrow();
	    repo.deleteById(id);
		return order;
	}
	
}
