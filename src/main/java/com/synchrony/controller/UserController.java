package com.synchrony.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synchrony.dto.UserDto;
import com.synchrony.entity.User;
import com.synchrony.service.UserService;

@RestController()
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody UserDto userdto){
		return ResponseEntity.ok(userService.save(modelMapper.map(userdto, User.class)));
	}
	
	
}
