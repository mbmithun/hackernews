package com.insider.hackernews.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insider.hackernews.dto.model.CommentDto;
import com.insider.hackernews.dto.model.UserDto;
import com.insider.hackernews.model.User;
import com.insider.hackernews.repository.CommentRepository;
import com.insider.hackernews.repository.UserRepository;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private UserRepository userRepository;
	private static final ModelMapper mapper = new ModelMapper();
	private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

	/**
	 * Get the top comments for a given story id
	 * @param storyId The id of the story
	 * @param numComments The number of comments to retrieve
	 */
	@Override
	public List<CommentDto> getTopCommentsByStory(Integer storyId, Integer numComments) {
		logger.trace("Fetching top comments for story {}", new Object[] {storyId});
		return commentRepository.getTopCommentsByStoryId(storyId, numComments).parallelStream()
			.filter(comment -> comment != null && comment.getText() != null)
			.map(comment -> {
				CommentDto commentDto = mapper.map(comment, CommentDto.class);
				User user = userRepository.getUserById(comment.getBy());
				commentDto.setUser(new UserDto(user));
				return commentDto;
			})
			.collect(Collectors.toList());
	}
}
