package com.project.BookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.project.BookStore.model.orderDetails;
import com.project.BookStore.model.orderDetailsDummy;
import com.project.BookStore.service.orderDetailService;


@RestController
public class orderDetailController {
	
	@Autowired
	private orderDetailService service;
	
	
	@PostMapping("/placeorder")
	public ResponseEntity<?> placeAnOrder(@RequestBody orderDetailsDummy order){
		try {
			return new ResponseEntity<>(service.placeAnOrder(order),HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>("500 Internal server Error\n -> check either entered non excisting Order Id\n -> check either entered all fields",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/vieworder/{id}")
	public ResponseEntity<?> viewOrder(@PathVariable int id){
		try {
			return new ResponseEntity<>(service.viewOrder(id),HttpStatus.FOUND);
		}
		catch(Exception e){
			return new ResponseEntity<>("404 No Order Found With The Specified Order Id",HttpStatus.OK);
		}
	}
	
	
	@GetMapping("/viewallorders")
	public List<orderDetails> viewAllOrders(){
		return service.viewAllOrders();
	}
	
	
	@PutMapping("/updateorder/{Id}")
	public ResponseEntity<?> updateOrder(@RequestBody orderDetailsDummy order, @PathVariable int Id){
		try {
			return new ResponseEntity<>(service.updateOrder(order,Id),HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>("500 Internal Server Error Unable To Update. \n ->Check Customer Or Book With The Specified Id Excists.",HttpStatus.INTERNAL_SERVER_ERROR );
		}
	}
	
	
	@DeleteMapping("/deleteorder/{id}")
	public ResponseEntity<?> deleteOrder(@PathVariable int id){
		try {
			return new ResponseEntity<>(service.deleteOrder(id),HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>("404 Order Details Not Found.\n ->Unable To Delete.",HttpStatus.NOT_FOUND );
		}
	}
	
}
