package com.project.BookStore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class homeController {
	@GetMapping("/")
	public String Home() {
		return "Home Page Of BookStore";
	}
}
