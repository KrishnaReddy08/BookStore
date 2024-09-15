package com.project.BookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.BookStore.model.book;
import com.project.BookStore.service.bookDetailService;

@RestController
public class bookDetailController {
	
	@Autowired
	private bookDetailService service;
	
	@PostMapping("/addbook")
	public ResponseEntity<?> addBook(@RequestBody book book) {
		try {
		return new ResponseEntity<>(service.addBook(book),HttpStatus.CREATED);
		}
		catch(Exception e){
			return new ResponseEntity<>("500 Internal server Error\n -> check either entered non excisting book Id & title\n -> check either entered all fields",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@GetMapping("/viewallbooks")
	public List<book> viewAllBooks(){
		return service.viewAllBooks();
	}
	
	
	@GetMapping("/viewbook/{Id}")
	public ResponseEntity<?> viewBookById(@PathVariable int Id) {
		try {
		return new ResponseEntity<>(service.viewBookById(Id),HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>("No book found with the Id",HttpStatus.NOT_FOUND);
		}
	}
	
	
	@GetMapping("/viewbookbytitle/{title}")
	public ResponseEntity<?> viewBookByTitle(@PathVariable String title) {
		System.out.println(service.viewBookByTitle(title));
			if(service.viewBookByTitle(title)!=null)
				return new ResponseEntity<>(service.viewBookByTitle(title),HttpStatus.FOUND);
			else return new ResponseEntity<>("No Book Found With The Title",HttpStatus.NOT_FOUND);
	}
	
	
	@PutMapping("/updatebook/{id}")
	public ResponseEntity<?> updateBook(@RequestBody book book,@PathVariable int id) {
		try {
			 return new ResponseEntity<>(service.updateBook(book,id),HttpStatus.OK);
		}
		
		catch(JpaSystemException e){
			return new ResponseEntity<>("Invalid data",HttpStatus.BAD_REQUEST);
		}
		catch(Exception e) {
			return new ResponseEntity<>("404 Book With the Specified Id Not Found",HttpStatus.NOT_FOUND);
		}
	}
	
	
	@DeleteMapping("/deletebook/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable int id) {
		try {
		return new ResponseEntity<>(service.deleteBookById(id),HttpStatus.OK);
		}
		catch (DataAccessException e) {
			return new ResponseEntity<>("Cannot delete a parent row",HttpStatus.BAD_REQUEST);
		}
		catch(Exception e){
			return new ResponseEntity<>("404 Book With The Specified Id Not Found",HttpStatus.NOT_FOUND);
		}
	}
	
	
	@DeleteMapping("/deletebookbytitle/{title}")
	public ResponseEntity<?> deleteBookByTitle(@PathVariable String title) {
		try {
		return new ResponseEntity<>(service.deleteBookByTitle(title),HttpStatus.OK);
		}
		catch (DataAccessException e) {
			return new ResponseEntity<>("Cannot delete a parent row",HttpStatus.BAD_REQUEST);
		}
		catch(Exception e){
			return new ResponseEntity<>("404 Book With The Specified Title Not Found",HttpStatus.NOT_FOUND);
		}
	}
		
}
