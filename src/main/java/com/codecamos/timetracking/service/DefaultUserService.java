package com.codecamos.timetracking.service;

import com.codecamos.timetracking.model.persistance.User;
import com.codecamos.timetracking.repo.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements IUserService {

	private UserRepository userRepository;

	@Autowired
	public DefaultUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User getUser(ObjectId id) {
		return userRepository.findById(id).get();
	}
}
