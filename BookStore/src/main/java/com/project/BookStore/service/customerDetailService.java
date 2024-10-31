package com.project.BookStore.service;


import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.BookStore.AuthenticatedUser.AuthenticatedUserDetails;
import com.project.BookStore.DTO.CustomerDetailDTO;
import com.project.BookStore.DTO.responseStructure;
import com.project.BookStore.exception.CustomerNotFoundException;
import com.project.BookStore.exception.InvalidPropertiesException;
import com.project.BookStore.exception.InvalidRequestException;
import com.project.BookStore.exception.UserNotFoundException;
import com.project.BookStore.model.customer;
import com.project.BookStore.model.userCredentials;
import com.project.BookStore.repository.customerRepo;
import com.project.BookStore.repository.userCredentialsRepo;

import jakarta.transaction.Transactional;


@Service
public class customerDetailService{
	
	@Autowired
	private customerRepo repo;
	
	@Autowired
	private userCredentialsRepo credentialsRepo;
	
	public CustomerDetailDTO CustomerDTOConvertor(customer Customer) {
		CustomerDetailDTO DTO = new CustomerDetailDTO();
		DTO.setCustomerId(Customer.getCustomerId());
		DTO.setName(Customer.getName());
		DTO.setEmail(Customer.getEmail());
		return DTO;
	}
	
	public boolean isValidEmail(String email) {
	    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,7}$";
	    Pattern pat = Pattern.compile(emailRegex);
	    return pat.matcher(email).matches();
	}
	
	public List<CustomerDetailDTO> convertToCustomerDetailTOList(List<customer> Customers) {
	    return Customers.stream()
	                 .map(this::CustomerDTOConvertor)
	                 .collect(Collectors.toList());
	}
	
	@Transactional
	public ResponseEntity<responseStructure<CustomerDetailDTO>> addCustomer(customer customer){	
		responseStructure<CustomerDetailDTO> structure = new responseStructure<CustomerDetailDTO>();
		Optional<userCredentials> UserCred = credentialsRepo.findById(customer.getCustomerId());
		if(repo.existsById(customer.getCustomerId())) throw new InvalidRequestException("Customer With Id "+customer.getCustomerId()+" Already Exists");
		if(UserCred.isEmpty()) throw new UserNotFoundException("No User With Id "+customer.getCustomerId()+" Exists");
		customer Customer = new customer();
		if(!isValidEmail(customer.getEmail())) throw new InvalidPropertiesException("Enter Valid Email");
		UserCred.get().setCustomer(Customer);
		Customer.setUserCredentials(UserCred.get());
		Customer.setCustomerId(customer.getCustomerId());
		Customer.setEmail(customer.getEmail());
		Customer.setName(customer.getName());
		credentialsRepo.save(UserCred.get());
		structure.setData(CustomerDTOConvertor(repo.save(Customer)));
		structure.setMessage("Added Successfully");
		structure.setStatus_code(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<responseStructure<CustomerDetailDTO>>(structure,HttpStatus.ACCEPTED);
	}
	
	
	public ResponseEntity<responseStructure<List<CustomerDetailDTO>>> viewAllCustomers() {
		responseStructure<List<CustomerDetailDTO>> structure = new responseStructure<List<CustomerDetailDTO>>();
		List<customer> customers = repo.findAll();
		if(customers.isEmpty()) throw new CustomerNotFoundException("No Customer Found");
		structure.setData(convertToCustomerDetailTOList(customers));
		structure.setMessage("Customers Found");
		structure.setStatus_code(HttpStatus.OK.value());
		return new ResponseEntity<responseStructure<List<CustomerDetailDTO>>>(structure,HttpStatus.OK);
	}
	
	
	public ResponseEntity<responseStructure<CustomerDetailDTO>> viewCustomerById(int id) {
		responseStructure<CustomerDetailDTO> structure = new responseStructure<CustomerDetailDTO>();
		Optional<customer> customer = repo.findById(id);
		if(customer.isPresent()) {
			structure.setData(CustomerDTOConvertor(customer.get()));
			structure.setMessage("Customer Found");
			structure.setStatus_code(HttpStatus.OK.value());
			return new ResponseEntity<responseStructure<CustomerDetailDTO>>(structure,HttpStatus.OK);
		}
		throw new CustomerNotFoundException("Customer With Id "+id+" Not Found");
	}
	
	
	public ResponseEntity<responseStructure<CustomerDetailDTO>> viewCurrentCustomer() {
		responseStructure<CustomerDetailDTO> structure = new responseStructure<CustomerDetailDTO>();
		int id =credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		Optional<customer> customer = repo.findById(id);
		if(customer.isPresent()) {
			structure.setData(CustomerDTOConvertor(customer.get()));
			structure.setMessage("Customer Found");
			structure.setStatus_code(HttpStatus.OK.value());
			return new ResponseEntity<responseStructure<CustomerDetailDTO>>(structure,HttpStatus.OK);
		}
		throw new CustomerNotFoundException("Customer With Id "+id+" Not Found");
	}

	
	public ResponseEntity<responseStructure<List<CustomerDetailDTO>>> viewCustomerByName(String name) {
		List<customer> customer = repo.findByName(name).get();
		responseStructure<List<CustomerDetailDTO>> structure = new responseStructure<List<CustomerDetailDTO>>();
		if(customer.isEmpty()) throw new CustomerNotFoundException("Customer With Name "+name+" Not Found");
		structure.setData(convertToCustomerDetailTOList(customer));
		structure.setMessage("Customers Found");
		structure.setStatus_code(HttpStatus.OK.value());
		return new ResponseEntity<responseStructure<List<CustomerDetailDTO>>>(structure,HttpStatus.OK);
	}
	
	
	public ResponseEntity<responseStructure<CustomerDetailDTO>> updateCustomerDetailAdmin(customer customer,int id) {
		responseStructure<CustomerDetailDTO> structure = new responseStructure<CustomerDetailDTO>();
		Optional<customer> OptionalCustomer = repo.findById(id);
		if(OptionalCustomer.isPresent()) {
			customer Customer = OptionalCustomer.get();
			if(customer.getName()==null) customer.setName(Customer.getName());
			if(customer.getEmail()==null) customer.setEmail(Customer.getEmail());
			if(!isValidEmail(customer.getEmail())) throw new InvalidPropertiesException("Enter Valid Email");
			Customer.setName(customer.getName());
			Customer.setEmail(customer.getEmail());
			structure.setData(CustomerDTOConvertor(repo.save(Customer)));
			structure.setMessage("Updated Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<CustomerDetailDTO>>(structure,HttpStatus.ACCEPTED);
		}
		throw new CustomerNotFoundException("Customer With Id "+id+" Not Found");
	}
	
	public ResponseEntity<responseStructure<CustomerDetailDTO>> updateCustomerDetail(customer customer) {
		responseStructure<CustomerDetailDTO> structure = new responseStructure<CustomerDetailDTO>();
		int id =credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		Optional<customer> OptionalCustomer = repo.findById(id);
		if(OptionalCustomer.isPresent()) {
			customer Customer = OptionalCustomer.get();
			if(customer.getName()==null) customer.setName(Customer.getName());
			if(customer.getEmail()==null) customer.setEmail(Customer.getEmail());
			if(isValidEmail(customer.getEmail())==false) throw new InvalidPropertiesException("Enter Valid Email");
			Customer.setName(customer.getName());
			Customer.setEmail(customer.getEmail());
			structure.setData(CustomerDTOConvertor(repo.save(Customer)));
			structure.setMessage("Updated Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<CustomerDetailDTO>>(structure,HttpStatus.ACCEPTED);
		}
		throw new CustomerNotFoundException("Customer With Id "+id+" Not Found");
	}
	
	
	public ResponseEntity<responseStructure<CustomerDetailDTO>> deleteCustomerById(int id){
		responseStructure<CustomerDetailDTO> structure = new responseStructure<CustomerDetailDTO>();
		Optional<customer> OptionalCustomer = repo.findById(id);
		if(OptionalCustomer.isPresent()) {
			customer Customer = OptionalCustomer.get();
			Optional<userCredentials> Credentials = credentialsRepo.findById(id);
			Credentials.get().setCustomer(null);
			credentialsRepo.save(Credentials.get());
			repo.deleteById(id);
			structure.setData(CustomerDTOConvertor(Customer));
			structure.setMessage("Deleted Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<CustomerDetailDTO>>(structure,HttpStatus.ACCEPTED);
		}
		throw new CustomerNotFoundException("Customer With Id "+id+" Not Found.Unable To Delete");
	}
	
	public ResponseEntity<responseStructure<CustomerDetailDTO>> deleteCurrentCustomer(){
		responseStructure<CustomerDetailDTO> structure = new responseStructure<CustomerDetailDTO>();
		int id =credentialsRepo.findByUsername(AuthenticatedUserDetails.getCurrentUser()).get().getCustomerId();
		Optional<customer> OptionalCustomer = repo.findById(id);
		if(OptionalCustomer.isPresent()) {
			customer Customer = OptionalCustomer.get();
			Optional<userCredentials> Credentials = credentialsRepo.findById(id);
			Credentials.get().setCustomer(null);
			credentialsRepo.save(Credentials.get());
			repo.deleteById(id);
			structure.setData(CustomerDTOConvertor(Customer));
			structure.setMessage("Deleted Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<CustomerDetailDTO>>(structure,HttpStatus.ACCEPTED);
		}
		throw new CustomerNotFoundException("Customer With Id "+id+" Not Found.Unable To Delete");
	}
	
	
	public ResponseEntity<responseStructure<List<CustomerDetailDTO>>> deleteCustomerByName(String name) {
		Optional<List<customer>> OptionalCustomer = repo.findByName(name);
		responseStructure<List<CustomerDetailDTO>> structure = new responseStructure<List<CustomerDetailDTO>>();
		if(OptionalCustomer.isPresent()) {
			List<customer> customer = OptionalCustomer.get();
			repo.deleteAllInBatch(customer);
			structure.setData(convertToCustomerDetailTOList(customer));
			structure.setMessage("Deleted Successfully");
			structure.setStatus_code(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<responseStructure<List<CustomerDetailDTO>>>(structure,HttpStatus.ACCEPTED);
		}
		throw new CustomerNotFoundException("Customer With Name "+name+" Not Found.Unable To Delete");
	}
}
