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
import com.project.BookStore.model.orderDetails;
import com.project.BookStore.model.orderDetailsDummy;
import com.project.BookStore.service.orderDetailService;


@RestController
public class orderDetailController {
	
	@Autowired
	private orderDetailService service;
	
	
	@PostMapping("/admin/placeorder")
	public ResponseEntity<responseStructure<orderDetails>> placeAnOrder(@RequestBody orderDetailsDummy order){
		return service.placeAnOrder(order);
	}
	
	@PostMapping("/placeorder")
	public ResponseEntity<responseStructure<orderDetails>> placeOrder(@RequestBody orderDetailsDummy order){
		return service.placeOrder(order);
	}
	
	
	@GetMapping("/admin/vieworder/{id}")
	public ResponseEntity<responseStructure<orderDetails>> viewOrder(@PathVariable int id){
		return service.viewOrder(id);
	}
	
	
	@GetMapping("/admin/viewallorders")
	public ResponseEntity<responseStructure<List<orderDetails>>> viewAllOrdersAdmin(){
		return service.viewAllOrdersAdmin();
	}
	

	@GetMapping("/viewallorders")
	public ResponseEntity<responseStructure<List<orderDetails>>> viewAllOrders(){
		return service.viewAllOrders();
	}

	
	@PutMapping("/admin/updateorder/{Id}")
	public ResponseEntity<responseStructure<orderDetails>> updateOrder(@RequestBody orderDetailsDummy order, @PathVariable int Id){
		return service.updateOrderAdmin(order, Id);
	}
	
	@PutMapping("/updateorder/{Id}")
	public ResponseEntity<responseStructure<orderDetails>> updateOrder(@PathVariable int Id, @RequestBody orderDetailsDummy order){
		return service.updateOrderUser(Id, order);
	}
	
	
	@DeleteMapping("/admin/deleteorder/{id}")
	public ResponseEntity<responseStructure<orderDetails>> deleteOrderAdmin(@PathVariable int id){
		return service.deleteOrderAdmin(id);
	}
	
	@DeleteMapping("/deleteorder/{id}")
	public ResponseEntity<responseStructure<orderDetails>> deleteOrder(@PathVariable int id){
		return service.deleteOrder(id);
	}
	
}
