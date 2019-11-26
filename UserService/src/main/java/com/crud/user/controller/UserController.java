package com.crud.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
		try {
			userDetailsService.saveUser(user);
			StringBuilder sb = new StringBuilder();
			sb.append(user.getFirstName()).append(" ").append(user.getLastName());
			return new ResponseEntity(sb, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity("Unable to add user!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	public ResponseEntity<?> getByEmailId(@RequestParam("emailId") String emailId) {
		try {
			User user = userDetailsService.findUserByEmail(emailId);
			if (user == null) {
				new ResponseEntity("User not found!", HttpStatus.OK);
			}
			user.setPassword("");
			return new ResponseEntity(user, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity("Failed to get user!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping
	public ResponseEntity<?> changePassword(@RequestParam("emailId") String emailId,
			@RequestParam("currentPassword") String currentPassword,
			@RequestParam("newPassword") String newPassword) {
		try {
			User user = userDetailsService.findUserByEmail(emailId);
			if (user == null) {
				new ResponseEntity("User not found!", HttpStatus.OK);
			}
			String message = userDetailsService.changePassword(user, currentPassword, newPassword);
			return new ResponseEntity(message, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity("Failed to change password!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteUser(@RequestParam("id") String id) {
		try {
			userDetailsService.deleteProduct(id);
			return new ResponseEntity("User deleted succcessfully", HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity("Unable to delete!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
