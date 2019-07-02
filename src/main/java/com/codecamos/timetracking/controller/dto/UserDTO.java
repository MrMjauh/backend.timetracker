package com.codecamos.timetracking.controller.dto;

import com.codecamos.timetracking.model.persistance.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {

	private String email;
	private HashSet<String> roles;
	private Date createdAt;
	private Date updatedAt;

	public static UserDTO from(User user) {
		return UserDTO
				.builder()
				.createdAt(user.getId().getDate())
				.updatedAt(user.getUpdatedAt())
				.email(user.getEmail())
				.roles(user.getRoles())
				.build();
	}
}
