package com.insider.hackernews.repository;

import com.insider.hackernews.model.User;

public interface UserRepository {
	public User getUserById(String userid);
}
