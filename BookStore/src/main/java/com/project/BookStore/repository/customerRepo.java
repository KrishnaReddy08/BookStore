package com.project.BookStore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.BookStore.model.customer;
import org.springframework.stereotype.Repository;

@Repository
public interface customerRepo extends JpaRepository<customer,Integer>{
	Optional<List<customer>> findByName(String name);

}
