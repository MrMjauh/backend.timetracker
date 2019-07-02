package com.codecamos.timetracking.config.auth;

import com.codecamos.timetracking.model.persistance.User;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.HashSet;

@Data
@Builder
@AllArgsConstructor
public class AuthenticatedUser {

	private ObjectId id;
	private HashSet<String> roles;

	public static AuthenticatedUser from(User user) {
		return AuthenticatedUser
				.builder()
				.id(user.getId())
				.roles(user.getRoles())
				.build();
	}
}
