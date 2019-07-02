package com.codecamos.timetracking.service;

import com.codecamos.timetracking.model.persistance.User;
import com.codecamos.timetracking.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTokenService implements ITokenService {

	private UserRepository userRepository;

	@Autowired
	public DefaultTokenService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User getUserFromToken(String token) {
		return this.userRepository.getUserByToken(token);
	}
}
