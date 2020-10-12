package com.insider.hackernews.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.insider.hackernews.dto.model.StoryDto;
import com.insider.hackernews.repository.StoryDocumentRepository;
import com.insider.hackernews.repository.StoryRepository;

@Service
public class StoryServiceImpl implements StoryService{

	@Autowired
	private StoryRepository storyRepository;
	@Autowired
	private StoryDocumentRepository storyDocumentRepository;
	private static final ModelMapper mapper = new ModelMapper();
	private static final Logger logger = LoggerFactory.getLogger(StoryServiceImpl.class);

	/**
	 * Get the top stories for the given count
	 * @param count - The number of stories to retrieve
	 */
	@Override
	public List<StoryDto> getTopStories(int count) {
		logger.trace("Fetching the top {} stories", new Object[] {count});
		return storyRepository.getTopStories(count).parallelStream()
				.filter(story -> story != null)
				.map((story) -> mapper.map(story, StoryDto.class))
				.collect(Collectors.toList());
	}

	/**
	 * Get the story for the given story id
	 * @param storyId - The story id
	 */
	@Override
	public StoryDto getStoryById(Integer storyId) {
		logger.trace("Fetching the story for id: {}", new Object[] {storyId});
		return mapper.map(storyRepository.getStoryById(storyId), StoryDto.class);
	}

	/**
	 * Get all the top stories that have been served so far
	 * @param pageable - Gives the page size and page number of the results
	 */
	@Override
	public List<StoryDto> getPastStories(Pageable pageable) {
		logger.trace("Fetching the past stories with page size {} and page number {}", 
				new Object[] {pageable.getPageSize(), pageable.getPageNumber()});
		return storyDocumentRepository.findAll(pageable).stream()
				.map((story) -> mapper.map(story, StoryDto.class))
				.collect(Collectors.toList());
	}
}
