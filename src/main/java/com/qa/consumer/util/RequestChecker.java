package com.qa.consumer.util;

import com.qa.consumer.persistence.repository.UserRepository;
import com.qa.persistence.domain.UserRequest;

public class RequestChecker {

	public static boolean isInvalid(UserRequest request) {
		return (request.getUserToAddOrUpdate() == null || request.getUsername() == null);
	}

	public static boolean userExists(UserRequest request, UserRepository repo) {
		return repo.findById(request.getUsername()).isPresent();
	}
}
