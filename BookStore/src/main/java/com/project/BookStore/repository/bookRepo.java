package com.project.BookStore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.BookStore.model.book;


@Repository
public interface bookRepo extends JpaRepository<book, Integer> {
	
	Optional<book> findByTitle(String title);
	boolean existsByTitle(String title);
}
