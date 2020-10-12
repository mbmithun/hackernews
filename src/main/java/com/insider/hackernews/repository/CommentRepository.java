package com.insider.hackernews.repository;

import java.util.List;

import com.insider.hackernews.model.Comment;

public interface CommentRepository {

	List<Comment> getTopCommentsByStoryId(Integer storyId, Integer numComments);

	Comment getCommentById(Integer commentId);
}
