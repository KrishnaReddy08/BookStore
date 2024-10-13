package com.project.BookStore.AuthenticatedUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUserDetails {
	public static String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null && authentication.isAuthenticated()) {
			Object object = authentication.getPrincipal();
			if(object instanceof UserDetails) {
				return ((UserDetails)object).getUsername();
			}
		}
		return null;
	}
}
