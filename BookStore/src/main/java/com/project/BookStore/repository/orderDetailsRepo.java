package com.project.BookStore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.BookStore.model.customer;
import com.project.BookStore.model.orderDetails;


@Repository
public interface orderDetailsRepo extends JpaRepository<orderDetails, Integer> {
	
	//@Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END FROM order_details b WHERE b.book_id = :book_id")
	//boolean existsByBookId(@Param("book_id") int id);
	Optional<List<orderDetails>> findByCustomer(customer customer);
}
