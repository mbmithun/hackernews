package com.insider.hackernews.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.insider.hackernews.model.Story;
import com.insider.hackernews.service.RestService;

@Repository
public class StoryRepositoryImpl implements StoryRepository {

	@Autowired
	private RestService restService;
	@Autowired
	private StoryDocumentRepository storyDocumentRepository;

	/**
	 * Get the top n stories. As the top stories are retrieved, they are also
	 * updated into the DB to serve the past-stories API
	 * @param n - Number of top stories to fetch
	 */
	@Cacheable(value = "story")
	public List<Story> getTopStories(Integer n) {
		List<Integer> topStoryIds = getTopStoryIds(n);
		return getStories(topStoryIds).parallelStream()
				.filter(story -> story != null)
				.map(story -> {
					if (!storyDocumentRepository.existsById(story.getId())) {
						storyDocumentRepository.save(story);
					}
					return story;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Given a list of story ids, get all the stories
	 * @param storyIds - A list of story ids
	 */
	private List<Story> getStories(List<Integer> storyIds) {
		return storyIds.parallelStream().map(storyId -> getStoryById(storyId)).collect(Collectors.toList());
	}

	
	/**
	 * Get story for given id
	 * @param storyId - The story id
	 */
	@Override
	@Cacheable(value = "story", key = "#storyId")
	public Story getStoryById(Integer storyId) {
		Story story = restService.getWebClient().get().uri("/item/{id}.json", storyId).retrieve().bodyToMono(Story.class).block();
		return story;
	}

	/**
	 * Get the top n story ids
	 * 
	 * @param numStories The number of Ids to fetch
	 * @return a list of integer story ids
	 */
	@SuppressWarnings("unchecked")
	private List<Integer> getTopStoryIds(Integer numStories) {
		List<Integer> topIds = restService.getWebClient().get().uri("topstories.json").retrieve().bodyToMono(List.class).block();
		if (topIds.size() > numStories) {
			topIds = topIds.subList(0, numStories);
		}
		return topIds;
	}
}
