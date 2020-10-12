package com.insider.hackernews.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.google.gson.Gson;
import com.insider.hackernews.dto.model.StoryDto;
import com.insider.hackernews.model.Story;
import com.insider.hackernews.repository.StoryDocumentRepository;
import com.insider.hackernews.service.StoryService;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@SpringBootTest
public class StoryServiceMockTest {

	
	private static MockWebServer mockBackEnd;
	@Autowired
	private StoryService storyService;
	private static Gson gson;
	@MockBean
	private StoryDocumentRepository storyDocRepository;
	private static String jsonBody;
	@Autowired
	private CacheManager cacheManager;
	
	@BeforeAll
	private static void setup() throws IOException {
		mockBackEnd = new MockWebServer();
		mockBackEnd.start();
		gson = new Gson();
		jsonBody = readFromFile(new File("src/test/resources/story.json").toPath());
	}
	
	@BeforeEach
	private void before() {
		cacheManager.getCache("story").clear();
		storyDocRepository.deleteAll();
	}
	
	@AfterAll
	private static void tearDown() throws IOException {
		mockBackEnd.shutdown();
	}
	
	
	@Test
	@DisplayName("Test for get top 5 comments for a story")
	public void testGetTopStories() throws IOException {
		List<Integer> topStories = Arrays.asList(24748488, 24748488, 24748488, 24748488, 24748488);
		mockBackEnd.enqueue(new MockResponse().setBody(topStories.toString()));
		for (int i=0; i<topStories.size(); i++) {
			mockBackEnd.enqueue(new MockResponse().setBody(jsonBody));
		}
		Mockito.when(storyDocRepository.save(Mockito.any())).thenReturn(new Story());
		List<StoryDto> stories = storyService.getTopStories(5);
		Story story = gson.fromJson(jsonBody, Story.class);
		assertEquals(topStories.size(), stories.size());
	}
	
	@Test
	@DisplayName("Test for get past stories for no data in db")
	public void testGetPastStoriesNoData() throws IOException {
		
		Page<Story> page = new PageImpl<Story>(new ArrayList<Story>(), PageRequest.of(0, 10), 0);
		Mockito.when(storyDocRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
		List<StoryDto> stories = storyService.getPastStories(PageRequest.of(0, 10));
		assertEquals(0, stories.size());
	}
	
	@Test
	@DisplayName("Test for get past stories with data in db")
	public void testGetPastStoriesWithData() throws IOException {
		
		List<Story> stories = new ArrayList<>();
		for (int i=0; i<10; i++) {
			stories.add(gson.fromJson(jsonBody, Story.class));
		}
		
		Page<Story> page = new PageImpl<Story>(stories.subList(5, 10), PageRequest.of(0, 5), stories.size());
		Mockito.when(storyDocRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);
		List<StoryDto> resStories = storyService.getPastStories(PageRequest.of(0, 5));
		assertEquals(5, resStories.size());
	}
	
	private static String readFromFile(Path path) throws IOException {
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = Files.newBufferedReader(path)) {
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}
		return sb.toString();
	}
	
}
