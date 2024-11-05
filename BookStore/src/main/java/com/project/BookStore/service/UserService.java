package com.project.BookStore.service;

import com.project.BookStore.config.userDetailConfig;
import com.project.BookStore.exception.UserNotFoundException;
import com.project.BookStore.model.userCredentials;
import com.project.BookStore.repository.userCredentialsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private userCredentialsRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        Optional<userCredentials> credentials = repo.findByUsername(username);
        if(credentials.isPresent()) {
            return new userDetailConfig(credentials.get());
        }throw new UserNotFoundException("USER NOT FOUND");
    }
}
