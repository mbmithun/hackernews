package com.insider.hackernews.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insider.hackernews.dto.model.CommentDto;
import com.insider.hackernews.service.CommentService;

@RestController
public class CommentController {

	@Autowired
	private CommentService commentService;

	/**
	 * Get the top 10 comments for a given story id
	 * @param storyId The story id
	 * @return The top 10 comments. The comment count need not be 10 in some cases
	 */
	@GetMapping("api/v0/top-comments")
	public List<CommentDto> getTopCommentsByStory(@RequestParam(name="storyId", required=true) @Validated Integer storyId) {
		return commentService.getTopCommentsByStory(storyId, 10);
	}
}
