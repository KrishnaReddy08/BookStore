package com.project.BookStore.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.BookStore.AuthenticatedUser.AuthenticatedUserDetails;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.config.userDetailConfig;
import com.project.BookStore.exception.UserNotFoundException;
import com.project.BookStore.model.userCredentials;
import com.project.BookStore.repository.userCredentialsRepo;


@Service
public class userCredentialsService implements UserDetailsService {
	
	@Autowired
	private userCredentialsRepo repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
		Optional<userCredentials> credentials = repo.findByUsername(username);
		if(credentials.isPresent()) {
			return new userDetailConfig(credentials.get());
		}throw new UserNotFoundException("USER NOT FOUND");
	}
	
	public ResponseEntity<responseStructure<userCredentials>> viewUserAdmin(int Id){
		responseStructure<userCredentials> structure = new responseStructure<userCredentials>();
		structure.setData(repo.findById(Id).get());
		structure.setMessage("USER FOUND");
		structure.setStatus_code(HttpStatus.FOUND.value());
		return new ResponseEntity<responseStructure<userCredentials>>(structure,HttpStatus.FOUND);
	}
	
	public ResponseEntity<responseStructure<userCredentials>> viewUser(){
		responseStructure<userCredentials> structure = new responseStructure<userCredentials>();
		int Id =repo.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		structure.setData(repo.findById(Id).get());
		structure.setMessage("USER FOUND");
		structure.setStatus_code(HttpStatus.FOUND.value());
		return new ResponseEntity<responseStructure<userCredentials>>(structure,HttpStatus.FOUND);
	}
	
	public ResponseEntity<responseStructure<userCredentials>> addNewUser(userCredentials credentials){
		responseStructure<userCredentials> structure = new responseStructure<userCredentials>();
		structure.setData(repo.save(credentials));
		structure.setMessage("Added User");
		structure.setStatus_code(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<responseStructure<userCredentials>>(structure,HttpStatus.ACCEPTED);
	}
	
	public ResponseEntity<responseStructure<userCredentials>> UpdateUser(int Id, userCredentials credentials){
		responseStructure<userCredentials> structure = new responseStructure<userCredentials>();
		Optional<userCredentials> optionalCredentials = repo.findById(Id);
		if(optionalCredentials.isPresent()) {
			userCredentials Credentials = new userCredentials();
			Credentials.setCustomerId(Id);
			Credentials.setUsername(credentials.getUsername());
			Credentials.setPassword(credentials.getPassword());
			Credentials.setRoles(credentials.getRoles());
			structure.setData(repo.save(Credentials));
			structure.setMessage("User Credentials Updated");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<userCredentials>>(structure,HttpStatus.ACCEPTED);
		}
		throw new UsernameNotFoundException("Enter Existing And Valid User Credentials");
		
	}
	
	public ResponseEntity<responseStructure<userCredentials>> UpdateCurrentUser(userCredentials credentials){
		responseStructure<userCredentials> structure = new responseStructure<userCredentials>();
		int id = repo.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		Optional<userCredentials> optionalCredentials = repo.findById(id);
		if(optionalCredentials.isPresent()) {
			userCredentials Credentials = new userCredentials();
			Credentials.setCustomerId(id);
			Credentials.setUsername(credentials.getUsername());
			Credentials.setPassword(credentials.getPassword());
			structure.setData(repo.save(Credentials));
			structure.setMessage("User Credentials Updated");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<userCredentials>>(structure,HttpStatus.ACCEPTED);
		}
		throw new UsernameNotFoundException("Enter Existing And Valid User Credentials");
	}
	
	public ResponseEntity<responseStructure<userCredentials>> deleteUser(int Id){
		responseStructure<userCredentials> structure = new responseStructure<userCredentials>();
		Optional<userCredentials> optionalCredentials = repo.findById(Id);
		if(optionalCredentials.isPresent()) {
			repo.deleteById(Id);
			structure.setData(optionalCredentials.get());
			structure.setMessage("User Deleted");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<userCredentials>>(structure,HttpStatus.ACCEPTED);
		}throw new UsernameNotFoundException("Enter Existing And Valid User Credentials");
	}
	
	public ResponseEntity<responseStructure<userCredentials>> deleteCurrentUser(){
		responseStructure<userCredentials> structure = new responseStructure<userCredentials>();
		int Id = repo.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		Optional<userCredentials> optionalCredentials = repo.findById(Id);
		if(optionalCredentials.isPresent()) {
			repo.deleteById(Id);
			structure.setData(optionalCredentials.get());
			structure.setMessage("User Deleted");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<userCredentials>>(structure,HttpStatus.ACCEPTED);
		}throw new UsernameNotFoundException("Enter Existing And Valid User Credentials");
	}
}
