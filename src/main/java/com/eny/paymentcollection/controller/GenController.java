package com.eny.paymentcollection.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GenController {

	//TODO: remove this class it is for testing
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public String forUser() {

		return "welcome user";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String forAdmin() {

		return "welcome admin";
	}

}
