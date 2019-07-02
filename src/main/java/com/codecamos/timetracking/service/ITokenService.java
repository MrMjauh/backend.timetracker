package com.codecamos.timetracking.service;

import com.codecamos.timetracking.model.persistance.User;

public interface ITokenService {

	User getUserFromToken(String token);

}
