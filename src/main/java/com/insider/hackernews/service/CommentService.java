package com.insider.hackernews.service;

import java.util.List;

import com.insider.hackernews.dto.model.CommentDto;

public interface CommentService {

	public List<CommentDto> getTopCommentsByStory(Integer storyId, Integer numComments);
}
