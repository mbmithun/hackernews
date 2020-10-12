package com.insider.hackernews.repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.insider.hackernews.model.Comment;
import com.insider.hackernews.model.Story;
import com.insider.hackernews.service.RestService;

/**
 * A repository to manage all the comments to be retrieved
 * 
 * @author Mithun
 *
 */
@Repository
public class CommentRepositoryImpl implements CommentRepository {

	@Autowired
	private RestService restService;
	@Autowired
	private StoryRepository storyRepository;
	private static final Logger logger = LoggerFactory.getLogger(CommentRepositoryImpl.class);

	/**
	 * Get the top comments by story id
	 * @param storyId The id of the story
	 * @param numComments The number of comments to retrieve
	 */
	@Override
	@Cacheable(value="comment", key="#storyId")
	public List<Comment> getTopCommentsByStoryId(Integer storyId, Integer numComments) {
		logger.trace("Getting top comments for story: {}", new Object[] {storyId});
		Story story = storyRepository.getStoryById(storyId);
		if (story.isDeleted() || story.isDead() || story.getKids() == null) {
			return Collections.emptyList();
		}
		List<Integer> commentIds = story.getKids();
		if (commentIds.size() > numComments) {
			commentIds = commentIds.subList(0, numComments);
		}
		return commentIds.parallelStream().map(commentId -> getCommentById(commentId)).collect(Collectors.toList());
	}

	/**
	 * Given the comment, retrieve the comment
	 * 
	 * @param commentId The id of the comment
	 */
	@Override
	@Cacheable(value = "comment", key = "#commentId")
	public Comment getCommentById(Integer commentId) {
		logger.trace("Getting comment for id: {}", new Object[] { commentId });
		Comment comment = restService.getWebClient().get().uri("/item/{id}.json", commentId).retrieve()
				.bodyToMono(Comment.class).block();
		return comment;
	}
}
