package com.project.BookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.model.book;
import com.project.BookStore.repository.bookRepo;
import com.project.BookStore.service.bookDetailService;

@RestController
public class bookDetailController {
	
	@Autowired
	private bookDetailService service;
	@Autowired
	bookRepo repo;
	
	@PostMapping("/admin/addbook")
	public ResponseEntity<responseStructure<book>> addBook(@RequestBody book book) {
		System.out.println(repo.existsByTitle(book.getTitle()));
		return service.addBook(book);
	}
	
	
	@GetMapping("/viewallbooks")
	public ResponseEntity<responseStructure<List<book>>> viewAllBooks(){
		return service.viewAllBooks();
	}
	
	
	@GetMapping("/viewbook/{Id}")
	public ResponseEntity<responseStructure<book>> viewBookById(@PathVariable int Id) {
		return service.viewBookById(Id);
	}
	
	
	@GetMapping("/viewbookbytitle/{title}")
	public ResponseEntity<responseStructure<book>> viewBookByTitle(@PathVariable String title) {
		return service.viewBookByTitle(title);
	}
	
	
	@PutMapping("/admin/updatebook/{id}")
	public ResponseEntity<responseStructure<book>> updateBook(@RequestBody book book,@PathVariable int id) {
		return service.updateBook(book, id);
	}
	
	
	@DeleteMapping("/admin/deletebook/{id}")
	public ResponseEntity<responseStructure<book>> deleteBook(@PathVariable int id) {
		return service.deleteBookById(id);
	}
	
	
	@DeleteMapping("/admin/deletebookbytitle/{title}")
	public ResponseEntity<responseStructure<book>> deleteBookByTitle(@PathVariable String title) {
		return service.deleteBookByTitle(title);
	}
		
}
