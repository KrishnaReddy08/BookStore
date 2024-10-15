package com.project.BookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.model.userCredentials;
import com.project.BookStore.service.userCredentialsService;


@Controller
public class userCredentialsController {
	
	@Autowired
	private userCredentialsService service;
	
	@GetMapping("/admin/viewuser/{Id}")
	public ResponseEntity<responseStructure<userCredentials>> viewUser(@PathVariable int Id){
		return service.viewUserAdmin(Id);
	}
	
	@GetMapping("/viewcurrentuser")
	public ResponseEntity<responseStructure<userCredentials>> viewUser(){
		return service.viewUser();
	}
	
	@PostMapping("/admin/addnewuser")
	public ResponseEntity<responseStructure<userCredentials>> addNewUser(@RequestBody userCredentials credentials){
		return service.addNewUser(credentials);
	}
	
	@PutMapping("/admin/updateuser/{Id}")
	public ResponseEntity<responseStructure<userCredentials>> UpdateUser(@PathVariable int Id,@RequestBody userCredentials credentials){
		return service.UpdateUser(Id, credentials);
	}
	
	@PutMapping("/updateuser")
	public ResponseEntity<responseStructure<userCredentials>> UpdateUser(@RequestBody userCredentials credentials){
		return service.UpdateCurrentUser(credentials);
	}
	
	@DeleteMapping("/admin/deleteuser/{Id}")
	public ResponseEntity<responseStructure<userCredentials>> DeleteUserAdmin(@PathVariable int Id){
		return service.deleteUser(Id);
	}
	
	@DeleteMapping("/deletecurrentuser")
	public ResponseEntity<responseStructure<userCredentials>> DeleteUser(){
		return service.deleteCurrentUser();
	}
	
}
