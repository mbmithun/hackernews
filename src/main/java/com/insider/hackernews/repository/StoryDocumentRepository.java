package com.insider.hackernews.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.insider.hackernews.model.Story;

@Repository
public interface StoryDocumentRepository extends MongoRepository<Story, String> {
	
	public boolean existsById(Integer id);
}
