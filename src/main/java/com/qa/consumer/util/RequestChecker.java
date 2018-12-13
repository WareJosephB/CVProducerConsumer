package com.qa.consumer.util;

import com.qa.consumer.persistence.repository.CVRepository;
import com.qa.consumer.persistence.repository.UserRepository;
import com.qa.persistence.domain.CVRequest;
import com.qa.persistence.domain.UserRequest;

import org.springframework.beans.factory.annotation.Autowired;

public class RequestChecker {

	@Autowired
	private static CVRepository repo;

	public static boolean isInvalid(UserRequest request) {
		return (request.getUserToAddOrUpdate() == null || request.getUsername() == null);
	}

	public static boolean userExists(UserRequest request, UserRepository repo) {
		return repo.findById(request.getUsername()).isPresent();
	}

	public static boolean isInvalid(CVRequest request) {
		return (request.getCv() == null);
	}

	public static boolean cvExists(CVRequest request) {
		return repo.findById(request.getcvIDtoActUpon()).isPresent();
	}
}
