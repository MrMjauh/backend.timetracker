package com.codecamos.timetracking.repo;

import com.codecamos.timetracking.model.persistance.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, ObjectId> {

	@Query("{ 'token': ?0 }")
	User getUserByToken(String token);
}
