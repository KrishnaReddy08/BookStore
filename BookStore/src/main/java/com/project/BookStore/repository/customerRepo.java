package com.project.BookStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.BookStore.model.customer;


@Repository
public interface customerRepo extends JpaRepository<customer, Integer> {
	
	List<customer> findByName(String name);
}
