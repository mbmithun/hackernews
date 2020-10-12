package com.insider.hackernews.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;

import com.google.gson.Gson;
import com.insider.hackernews.dto.model.CommentDto;
import com.insider.hackernews.model.Story;
import com.insider.hackernews.repository.StoryDocumentRepository;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@SpringBootTest
public class CommentServiceMockTest {

	
	private static MockWebServer mockBackEnd;
	@Autowired
	private StoryService storyService;
	@Autowired
	private CommentService commentService;
	private static Gson gson;
	@MockBean
	private StoryDocumentRepository storyDocRepository;
	private static String storyJson;
	private static String commentJson;
	private static String userJson;
	@Autowired
	private CacheManager cacheManager;
	
	@BeforeAll
	private static void setup() throws IOException {
		mockBackEnd = new MockWebServer();
		mockBackEnd.start();
		gson = new Gson();
		storyJson = readFromFile(new File("src/test/resources/story.json").toPath());
		commentJson = readFromFile(new File("src/test/resources/comment.json").toPath());
		userJson = readFromFile(new File("src/test/resources/user.json").toPath());
	}
	
	@BeforeEach
	private void before() {
		cacheManager.getCache("comment").clear();
		storyDocRepository.deleteAll();
	}
	
	@AfterAll
	private static void tearDown() throws IOException {
		mockBackEnd.shutdown();
	}
	
	
	@Test
	@DisplayName("Test for get top 5 comments")
	public void testGetTopStories() throws IOException {
		Integer storyId = 24748488;
		List<Integer> topStories = Arrays.asList(24748488, 24748488, 24748488, 24748488, 24748488);
		mockBackEnd.enqueue(new MockResponse().setBody(storyJson));
		mockBackEnd.enqueue(new MockResponse().setBody(commentJson));
		mockBackEnd.enqueue(new MockResponse().setBody(userJson));

		List<CommentDto> comments = commentService.getTopCommentsByStory(storyId, 5);
		assertEquals(5, comments.size());
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
