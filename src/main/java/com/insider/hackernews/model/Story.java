package com.insider.hackernews.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Document(collection="top-stories")
public class Story {
	
	@Id
	@JsonIgnore
	private String storyId;
	
	@Indexed(unique = true)
	private Integer id;
	private String by;
	private Integer descendants;
	private List<Integer> kids;
	private Integer score;
	private Long time;
	private String title;
	private String url;
	private boolean deleted;
	private boolean dead;
}
