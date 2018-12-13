package com.qa.consumer.util;

import com.qa.persistence.domain.UserRequest;

public class RequestChecker {

	public static boolean isInvalid(UserRequest request) {
		if (request.getUserToAddOrUpdate() == null || request.getUserToAddOrUpdate().getUsername() == null) {
			return false;
		}
		return true;
	}
	
}
