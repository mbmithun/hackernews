package com.insider.hackernews.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insider.hackernews.dto.model.StoryDto;
import com.insider.hackernews.service.StoryService;

@RestController
public class StoryController {

	@Autowired
	private StoryService storyService;

	/**
	 * Retrieve the top 10 stories from Hackernews.
	 * @return The top 10 story objects
	 */
	@GetMapping("api/v0/top-stories")
	public List<StoryDto> getTopStories() {
		return storyService.getTopStories(10);
	}

	/**
	 * Get all the past top stories that have been served. This method
	 * supports paging. The page number and page size can be provided.
	 * @param page The page number. Defaults to 0.
	 * @param size The page size. Defaults to 10
	 * @return The past top stories
	 * @throws IllegalArgumentException Invalid inputs for page/size
	 */
	@GetMapping(value="api/v0/past-stories")
	public List<StoryDto> getPastStories(@RequestParam(name="page", defaultValue="0") int page, 
			@RequestParam(value="size", defaultValue="10") int size) throws IllegalArgumentException {
		if (size < 0 || page < 0) {
			throw new IllegalArgumentException("Size or page number is negative");
		}
		return storyService.getPastStories(PageRequest.of(page, size));
	}
}
