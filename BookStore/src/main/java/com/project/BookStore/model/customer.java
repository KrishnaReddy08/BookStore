package com.project.BookStore.model;


import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


@Entity
public class customer {
	
	@Id
	private int customerId;
	
	@MapsId("customerId")
	@OneToOne
	@JoinColumn(name = "customerId")
	private userCredentials userCredentials;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String email;
	
	@JsonBackReference
	@OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
	private List<orderDetails> orderdetails;
	
	
	public userCredentials getUserCredentials() {
		return userCredentials;
	}
	public void setUserCredentials(userCredentials userCredentials) {
		this.userCredentials = userCredentials;
	}
	
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
		return "customer [customerId=" + customerId + ", userCredentials=" + userCredentials + ", name=" + name
				+ ", email=" + email + ", orderdetails=" + orderdetails + "]";
	}
	
}
