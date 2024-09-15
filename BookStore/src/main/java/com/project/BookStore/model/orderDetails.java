package com.project.BookStore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class orderDetails {
	
	@Id
	private int orderId;
	
	
	@ManyToOne
	@JoinColumn(name = "bookId",nullable = false)
	private book book;
	

	@ManyToOne
	@JoinColumn(name = "customerId",nullable = false)
	private customer customer;
	
	
	@Column(nullable = false)
	private int quantity;
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orederId) {
		this.orderId = orederId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public book getBook() {
		return book;
	}
	public void setBook(book book) {
		this.book = book;
	}
	public customer getCustomer() {
		return customer;
	}
	public void setCustomer(customer customer) {
		this.customer = customer;
	}
	
}
