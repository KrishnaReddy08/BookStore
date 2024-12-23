package com.project.BookStore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.exception.BookNotFoundException;
import com.project.BookStore.exception.InvalidPropertiesException;
import com.project.BookStore.model.book;
import com.project.BookStore.repository.bookRepo;

import jakarta.transaction.Transactional;


@Service
public class bookDetailService {
	
	@Autowired
	private bookRepo repo;
	
	public ResponseEntity<responseStructure<book>> addBook(book book){
		responseStructure<book> structure = new responseStructure<book>();
		if(repo.existsByTitle(book.getTitle())) throw new InvalidPropertiesException("Add a Non Existing Title");
		structure.setData(repo.save(book));
		structure.setMessage("Added Successfully");
		structure.setStatus_code(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<responseStructure<book>>(structure,HttpStatus.ACCEPTED);
	}
	

	public ResponseEntity<responseStructure<List<book>>> viewAllBooks() {
		responseStructure<List<book>> structure = new responseStructure<List<book>>();
		List<book> books = repo.findAll();
		if(books.isEmpty()) throw new BookNotFoundException("No Book Found");
		structure.setData(books);
		structure.setMessage("Books Found");
		structure.setStatus_code(HttpStatus.OK.value());
		return new ResponseEntity<responseStructure<List<book>>>(structure,HttpStatus.OK);
	}

	
	public ResponseEntity<responseStructure<book>> viewBookById(int id) {
		responseStructure<book> structure = new responseStructure<book>();
		Optional<book> OptionalBook = repo.findById(id);
		if(OptionalBook.isPresent()) {
			book Book = OptionalBook.get();
			structure.setData(Book);
			structure.setMessage("Book Found");
			structure.setStatus_code(HttpStatus.OK.value());
			return new ResponseEntity<responseStructure<book>>(structure,HttpStatus.OK);
		}throw new BookNotFoundException("Book With Id "+id+" Not Found");
	}

	
	public ResponseEntity<responseStructure<book>> viewBookByTitle(String title) {
			responseStructure<book> structure = new responseStructure<book>();
			Optional<book> OptionalBook = repo.findByTitle(title);
			if(OptionalBook.isPresent()) {
				book Book = OptionalBook.get();
				structure.setData(Book);
				structure.setMessage("Book Found");
				structure.setStatus_code(HttpStatus.OK.value());
				return new ResponseEntity<responseStructure<book>>(structure,HttpStatus.OK);
			}throw new BookNotFoundException("Book With Title "+title+" Not Found");
	}

	
	public ResponseEntity<responseStructure<book>> updateBook(book book ,int id) {
		responseStructure<book> structure = new responseStructure<book>();
		if(repo.existsByTitle(book.getTitle())) throw new InvalidPropertiesException("Add a Non Existing Title");
		Optional<book> OptionalBook = repo.findById(id);
		if(OptionalBook.isPresent()) {
			book Book = OptionalBook.get();
			if(book.getAuthor()==null) book.setAuthor(Book.getAuthor());
			if(book.getPrice()==0) book.setPrice(Book.getPrice());
			if(book.getTitle()==null) book.setTitle(Book.getTitle());
			if(book.getQuantity()==0) book.setQuantity(Book.getQuantity());
			Book.setBookId(id);
			Book.setAuthor(book.getAuthor());
			Book.setTitle(book.getTitle());
			Book.setPrice(book.getPrice());
			structure.setData(repo.save(Book));
			structure.setMessage("Updated Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<book>>(structure,HttpStatus.ACCEPTED);
		}throw new BookNotFoundException("Book With Id "+id+" Not Found.Unable To Update");
		
	}
	
	@Transactional
	public ResponseEntity<responseStructure<book>> deleteBookById(int id) {
		responseStructure<book> structure = new responseStructure<book>();
		Optional<book> OptionalBook =repo.findById(id);
		if(OptionalBook.isPresent()) {
			book Book = OptionalBook.get();
			repo.delete(Book);
			structure.setData(Book);
			structure.setMessage("Deleted Successfully");
			structure.setStatus_code(HttpStatus.OK.value());
			return new ResponseEntity<responseStructure<book>>(structure,HttpStatus.OK);
		}throw new BookNotFoundException("Book With The Id "+id+" Not Found.Unable To Delete");
	}
	
	@Transactional
	public ResponseEntity<responseStructure<book>> deleteBookByTitle(String title) {
		responseStructure<book> structure = new responseStructure<book>();
		Optional<book> OptionalBook =repo.findByTitle(title);
		if(OptionalBook.isPresent()) {
			book Book = OptionalBook.get();
			repo.delete(Book);
			structure.setData(Book);
			structure.setMessage("Deleted Successfully");
			structure.setStatus_code(HttpStatus.OK.value());
			return new ResponseEntity<responseStructure<book>>(structure,HttpStatus.OK);
		}throw new BookNotFoundException("Book With The Title "+title+" Not Found.Unable To Delete");
	}

}