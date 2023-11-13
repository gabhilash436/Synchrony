package com.synchrony.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synchrony.entity.User;
import com.synchrony.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public User findById(String id) {
		return userRepository.findById(Long.valueOf(id))
				.orElseThrow(()-> new IllegalArgumentException(String.format("user with %s not found", id)));
	}

	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}
	
}
