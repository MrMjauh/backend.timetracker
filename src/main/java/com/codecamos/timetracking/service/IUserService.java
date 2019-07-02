package com.codecamos.timetracking.service;

import com.codecamos.timetracking.model.persistance.User;
import org.bson.types.ObjectId;

public interface IUserService {

	User getUser(ObjectId id);
}
