package com.project.BookStore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.BookStore.model.customer;
import com.project.BookStore.model.orderDetails;


@Repository
public interface orderDetailsRepo extends JpaRepository<orderDetails, Integer> {
	Optional<List<orderDetails>> findByCustomer(customer customer);
}
