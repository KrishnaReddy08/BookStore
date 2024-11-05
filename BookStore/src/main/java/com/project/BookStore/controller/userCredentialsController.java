package com.project.BookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.model.userCredentials;
import com.project.BookStore.service.userCredentialsService;

import java.util.List;


@RestController
public class userCredentialsController {
	
	@Autowired
	private userCredentialsService service;

	@GetMapping("/admin/viewallusers")
	public ResponseEntity<responseStructure<List<userCredentials>>> ViewAllUsers(){
		return service.viewAllUsers();
	}

	@GetMapping("/admin/viewuser/{Id}")
	public ResponseEntity<responseStructure<userCredentials>> ViewUserAdmin(@PathVariable int Id){
		return service.viewUserAdmin(Id);
	}
	
	@GetMapping("/viewcurrentuser")
	public ResponseEntity<responseStructure<userCredentials>> ViewCurrentUser(){
		return service.viewCurrentUser();
	}

	@PostMapping("/login")
	public ResponseEntity<responseStructure<String>> login(@RequestBody userCredentials user){
		return service.verify(user);
	}
	
	@PostMapping("/admin/addnewuser")
	public ResponseEntity<responseStructure<userCredentials>> addNewUser(@RequestBody userCredentials credentials){
		return service.addNewUser(credentials);
	}
	
	@PutMapping("/admin/updateuser/{Id}")
	public ResponseEntity<responseStructure<userCredentials>> UpdateUser(@PathVariable int Id,@RequestBody userCredentials credentials){
		return service.UpdateUser(Id, credentials);
	}
	
	@PutMapping("/updatecurrentuser")
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
