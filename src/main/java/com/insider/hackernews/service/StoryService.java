package com.insider.hackernews.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.insider.hackernews.dto.model.StoryDto;

public interface StoryService {

	public List<StoryDto> getTopStories(int count);
	
	public StoryDto getStoryById(Integer storyId);

	public List<StoryDto> getPastStories(Pageable pageable);
}
