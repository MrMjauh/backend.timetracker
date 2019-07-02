package com.codecamos.timetracking.model.persistance;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Document(collection = "user")
public class User extends BaseEntity {

	@Field("email")
	@Indexed(unique = true)
	private String email;

	@Field("token")
	@Indexed(unique = true)
	private String token;

	@Field("roles")
	private HashSet<String> roles;
}
