package com.project.BookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.BookStore.model.orderDetails;


@Repository
public interface orderDetailsRepo extends JpaRepository<orderDetails, Integer> {

}
