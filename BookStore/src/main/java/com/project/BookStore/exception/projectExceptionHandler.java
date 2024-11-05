package com.project.BookStore.exception;

import org.hibernate.id.IdentifierGenerationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.project.BookStore.DTO.responseStructure;

import java.security.SignatureException;

@ControllerAdvice
public class projectExceptionHandler extends ResponseEntityExceptionHandler  {
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<responseStructure<String>> handleCustomerNotFoundException(CustomerNotFoundException exception){
		responseStructure<String> structure = new responseStructure<String>();
		structure.setData("Customer Not found");
		structure.setMessage(exception.getMessage());
		structure.setStatus_code(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<responseStructure<String>>(structure,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BookNotFoundException.class)
	public ResponseEntity<responseStructure<String>> handleBookNotFoundException(BookNotFoundException exception){
		responseStructure<String> structure = new responseStructure<String>();
		structure.setData("Book Not Found");
		structure.setMessage(exception.getMessage());
		structure.setStatus_code(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<responseStructure<String>>(structure,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<responseStructure<String>> handleOrderNotFoundException(OrderNotFoundException exception){
		responseStructure<String> structure = new responseStructure<String>();
		structure.setData("Order Not Found");
		structure.setMessage(exception.getMessage());
		structure.setStatus_code(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<responseStructure<String>>(structure,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidPropertiesException.class)
	public ResponseEntity<responseStructure<String>> handlenInvalidPropertiesException(InvalidPropertiesException exception){
		responseStructure<String> structure = new responseStructure<String>();
		structure.setData("Invalid Properties");
		structure.setMessage(exception.getMessage());
		structure.setStatus_code(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<responseStructure<String>>(structure,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<responseStructure<String>> handleUserNotFoundException(UserNotFoundException exception){
		responseStructure<String> structure = new responseStructure<String>();
		structure.setData("User Not Found");
		structure.setMessage(exception.getMessage());
		structure.setStatus_code(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<responseStructure<String>>(structure,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<responseStructure<String>> handleInvalidRequestException(InvalidRequestException exception){
		responseStructure<String> structure = new responseStructure<String>();
		structure.setData("Inavlid Request");
		structure.setMessage(exception.getMessage());
		structure.setStatus_code(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<responseStructure<String>>(structure,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<responseStructure<String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception){
		responseStructure<String> structure = new responseStructure<String>();
		structure.setData("Invalid Input");
		structure.setMessage("Invalid Type Of Paremeter");
		structure.setStatus_code(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<responseStructure<String>>(structure,HttpStatus.BAD_REQUEST);
		}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<responseStructure<String>> handleDataIntegrityViolationException(DataIntegrityViolationException exception){
		responseStructure<String> structure = new responseStructure<String>();
		structure.setData("Null Values And Dupliate Id Not Accepted");
		structure.setMessage(exception.getMessage());
		structure.setStatus_code(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<responseStructure<String>>(structure,HttpStatus.BAD_REQUEST);
		}
	
	@ExceptionHandler(IdentifierGenerationException.class)
	public ResponseEntity<responseStructure<String>> handleIdentifierGenerationException(IdentifierGenerationException exception){
		responseStructure<String> structure = new responseStructure<String>();
		structure.setData("CANT ADD A ENTRY WITHOUT ID");
		structure.setMessage(exception.getMessage());
		structure.setStatus_code(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<responseStructure<String>>(structure,HttpStatus.BAD_REQUEST);
		}

	@ExceptionHandler(InvalidJwtTokenSignatureException.class)
	public ResponseEntity<responseStructure<String>> handleSignatureException(InvalidJwtTokenSignatureException exception){
		responseStructure<String> structure = new responseStructure<>();
		System.err.println("JWT SignatureException caught: " + exception.getMessage());
		structure.setData("INVALID TOKEN ");
		structure.setMessage(exception.getMessage());
		structure.setStatus_code(HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<responseStructure<String>>(structure,HttpStatus.FORBIDDEN);
	}
	
}
