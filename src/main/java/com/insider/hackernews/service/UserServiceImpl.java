package com.insider.hackernews.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insider.hackernews.model.User;
import com.insider.hackernews.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User getUserById(String userid) {
		return userRepository.getUserById(userid);
	}
}
