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

import com.project.BookStore.DTO.OrderDetailDTO;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.model.orderDetails;
import com.project.BookStore.service.orderDetailService;


@RestController
public class orderDetailController {
	
	@Autowired
	private orderDetailService service;
	
	
	@PostMapping("/admin/placeorder")
	public ResponseEntity<responseStructure<OrderDetailDTO>> placeAnOrder(@RequestBody OrderDetailDTO order){
		return service.placeOrderAdmin(order);
	}
	
	@PostMapping("/placeorder")
	public ResponseEntity<responseStructure<OrderDetailDTO>> placeOrder(@RequestBody OrderDetailDTO order){
		return service.placeOrder(order);
	}
	
	
	@GetMapping("/admin/vieworder/{id}")
	public ResponseEntity<responseStructure<OrderDetailDTO>> viewOrderAdmin(@PathVariable int id){
		return service.viewOrderAdmin(id);
	}
	
	@GetMapping("/vieworder/{id}")
	public ResponseEntity<responseStructure<OrderDetailDTO>> viewOrder(@PathVariable int id){
		return service.viewOrder(id);
	}
	
	@GetMapping("/admin/viewallorders")
	public ResponseEntity<responseStructure<List<OrderDetailDTO>>> viewAllOrdersAdmin(){
		return service.viewAllOrdersAdmin();
	}
	

	@GetMapping("/viewallorders")
	public ResponseEntity<responseStructure<List<OrderDetailDTO>>> viewAllOrders(){
		return service.viewAllOrders();
	}

	
	@PutMapping("/admin/updateorder/{Id}")
	public ResponseEntity<responseStructure<OrderDetailDTO>> updateOrder(@RequestBody OrderDetailDTO order, @PathVariable int Id){
		return service.updateOrderAdmin(order, Id);
	}
	
	@PutMapping("/updateorder/{Id}")
	public ResponseEntity<responseStructure<OrderDetailDTO>> updateOrder(@PathVariable int Id, @RequestBody OrderDetailDTO order){
		return service.updateOrderUser(Id, order);
	}
	
	
	@DeleteMapping("/admin/deleteorder/{id}")
	public ResponseEntity<responseStructure<OrderDetailDTO>> deleteOrderAdmin(@PathVariable int id){
		return service.deleteOrderAdmin(id);
	}
	
	@DeleteMapping("/deleteorder/{id}")
	public ResponseEntity<responseStructure<OrderDetailDTO>> deleteOrder(@PathVariable int id){
		return service.deleteOrder(id);
	}
	
}
