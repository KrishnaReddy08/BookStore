package com.project.BookStore.model;

import org.springframework.stereotype.Component;

@Component
public class CustomerDummy {
		private int customerId;
		private String name;
		private String email;
		public int getCustomerId() {
			return customerId;
		}
		public void setCustomerId(int customerId) {
			this.customerId = customerId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		@Override
		public String toString() {
			return "CustomerDummy [customerId=" + customerId + ", name=" + name + ", email=" + email + "]";
		}
		
		
 
}
