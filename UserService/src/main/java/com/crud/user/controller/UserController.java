package com.crud.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crud.user.entity.User;
import com.crud.user.service.UserDetailService;


@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserDetailService userDetailsService;
	
	@PostMapping
	public ResponseEntity<?> addorUpdateUser(@RequestBody User user) {
		userDetailsService.saveUser(user);
		return new ResponseEntity("User added succcessfully", HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity(userDetailsService.findAll(), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<?> deleteUser(@RequestParam("id") String id) {
		userDetailsService.deleteProduct(id);
		return new ResponseEntity("User deleted succcessfully", HttpStatus.OK);
	}

}
