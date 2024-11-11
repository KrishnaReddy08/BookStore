package com.project.BookStore.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@Entity
public class book {
	
	@Id
	private int bookId;
	
	@Column(nullable = false,unique = true)
	private String title;
	
	@Column(nullable = false)
	private String author;
	
	@Column(nullable = false)
	private Float price;
	
	@Column(nullable = false)
	private int quantity;
	
	@JsonBackReference
	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
	private List<orderDetails> orderdetails;
	
	
	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
	public List<orderDetails> getOrderdetails() {
		return orderdetails;
	}
	public void setOrderdetails(List<orderDetails> orderdetails) {
		this.orderdetails = orderdetails;
	}
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
