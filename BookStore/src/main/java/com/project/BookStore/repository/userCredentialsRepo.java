package com.project.BookStore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.BookStore.model.userCredentials;

public interface userCredentialsRepo extends JpaRepository<userCredentials,Integer> {
	Optional<userCredentials> findByUsername(String username);
}
