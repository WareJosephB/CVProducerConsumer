package com.qa.consumer.util;

import com.qa.persistence.domain.UserRequest;

public class RequestChecker {

	public static boolean isInvalid(UserRequest request) {
		return (request.getUserToAddOrUpdate() == null || request.getUsername() == null);

	}
}
