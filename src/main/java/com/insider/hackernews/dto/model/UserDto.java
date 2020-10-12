package com.insider.hackernews.dto.model;

import java.time.Duration;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.insider.hackernews.model.User;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain=true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

	private String userHandle; // User Handle
	@JsonIgnore
	private Long created; // epoch in seconds
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Long profileAge;
	
	public UserDto(User user) {
		if (user != null) {
			this.userHandle = user.getId();
			this.created = user.getCreated();
		}
	}

	public Long getProfileAge() {
		if (created != null) {
			long days = Duration.between(Instant.ofEpochSecond(created), Instant.now()).toDays();
			profileAge = Long.divideUnsigned(days, 365);
		}
		return profileAge;
	}
}
