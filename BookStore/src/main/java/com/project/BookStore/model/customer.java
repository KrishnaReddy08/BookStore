package com.project.BookStore.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;



@Entity
public class customer {
	
	@Id
	private int customerId;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String email;
	
	@JsonBackReference
	@OneToMany(mappedBy = "customer",cascade = {CascadeType.DETACH,
												CascadeType.MERGE,
												CascadeType.PERSIST,
												CascadeType.REFRESH})
	private List<orderDetails> orderdetails;
	
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<orderDetails> getOrderdetails() {
		return orderdetails;
	}
	public void setOrderdetails(List<orderDetails> orderdetails) {
		this.orderdetails = orderdetails;
	}
	
	
	@Override
	public String toString() {
		return "customer [customerId=" + customerId + ", name=" + name + ", email=" + email + "]";
	}
	
	
}
