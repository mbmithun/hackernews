package com.insider.hackernews.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.insider.hackernews.model.User;
import com.insider.hackernews.service.RestService;

@Repository
public class UserRepositoryImpl implements UserRepository {
	
	@Autowired
	private RestService restService;
	
	@Override
	@Cacheable(value="user", key="#userid")
	public User getUserById(String userid) {
		return restService.getWebClient().get().uri("/user/{userHandle}.json", userid).retrieve().bodyToMono(User.class).block();
	}

}
