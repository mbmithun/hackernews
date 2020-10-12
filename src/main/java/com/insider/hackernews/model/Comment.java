package com.insider.hackernews.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain=true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

	private String by;
	private Integer id;
	private List<Integer> kids;
	private Integer parent;
	private String text;
	private Long time;
	private boolean deleted;
	private boolean dead;
}
