package com.insider.hackernews.repository;

import java.util.List;

import com.insider.hackernews.model.Story;

public interface StoryRepository {

	Story getStoryById(Integer storyId);

	List<Story> getTopStories(Integer i);
}
