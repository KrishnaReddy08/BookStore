package com.project.BookStore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.BookStore.model.book;
import com.project.BookStore.repository.bookRepo;


@Service
public class bookDetailService {
	
	@Autowired
	private bookRepo repo;
	
	public book addBook(book book) {
		return repo.save(book);
	}
	

	public List<book> viewAllBooks() {
		return repo.findAll();
	}

	
	public book viewBookById(int id) {
		return repo.findById(id).orElseThrow();
	}

	
	public book viewBookByTitle(String title) {
			return repo.findByTitle(title);
	}

	
	public book updateBook(book book ,int id) {
		book Book = repo.findById(id).orElseThrow();
		Book.setBookId(id);
		Book.setAuthor(book.getAuthor());
		Book.setTitle(book.getTitle());
		Book.setPrice(book.getPrice());
		return repo.save(Book);
	}
	
	
	public book deleteBookById(int id) {
		book Book =repo.findById(id).orElseThrow();
		repo.deleteById(id);
		return Book;
		
	}
	
	
	public book deleteBookByTitle(String title) {
		book Book =repo.findByTitle(title);
		repo.delete(Book);
		return Book;
		
	}
	
	
}
