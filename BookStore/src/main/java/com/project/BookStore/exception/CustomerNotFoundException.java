package com.project.BookStore.exception;

public class CustomerNotFoundException extends RuntimeException {
	public CustomerNotFoundException(String message) {
		super(message);
	}
}
