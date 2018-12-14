package com.qa.consumer.util;

import com.qa.consumer.persistence.repository.CVRepository;
import com.qa.consumer.persistence.repository.UserRepository;
import com.qa.persistence.domain.CV;
import com.qa.persistence.domain.CVRequest;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

public class RequestChecker {


	public static boolean isValid(UserRequest request) {
		return !(request.getUserToAddOrUpdate() == null || request.getUsername() == null);
	}
	
	public static boolean isValidDelete(UserRequest request) {
		return !(request.getUserToAddOrUpdate() == null || request.getUsername() == null);
	}

	public static boolean userExists(UserRequest request, UserRepository repo) {
		return repo.findById(request.getUsername()).isPresent();
	}

	public static boolean isValid(CVRequest request) {
		return !(request.getCv() == null);
	}

	public static boolean isValidDelete(CVRequest request) {
		return !(0 == request.getcvIDtoActUpon());
	}

	public static boolean cvExists(CVRequest request, CVRepository repo) {
		return repo.findById(request.getcvIDtoActUpon()).isPresent();
	}

	public static boolean validSearch(CVRequest request) {
		return (request.getSearchString() != null);
	}

	public static Iterable<User> multiError(UserRequest request) {
		ArrayList<User> errorList = new ArrayList<>();
		errorList.add(singleError(request).get());
		return errorList;
	}

	public static Optional<User> singleError(UserRequest request) {
		User errorMessage = new Trainee();
		errorMessage.setFirstName(errorMessage(request));
		return Optional.of(errorMessage);
	}

	public static String errorMessage(UserRequest request) {
		if (RequestChecker.isValid(request)) {
			return Constants.USER_NOT_FOUND_MESSAGE;
		}
		return Constants.MALFORMED_REQUEST_MESSAGE;
	}

	public static Iterable<CV> multiError(CVRequest request) {
		ArrayList<CV> errorList = new ArrayList<>();
		errorList.add(singleError(request).get());
		return errorList;
	}

	public static Optional<CV> singleError(CVRequest request) {
		return singleError(errorMessage(request));
	}

	public static Optional<CV> singleError(String error) {
		CV errorMessage = new CV();
		errorMessage.setErrorMessage(error);
		return Optional.of(errorMessage);
	}

	public static String errorMessage(CVRequest request) {
		if (RequestChecker.isValid(request)) {
			return Constants.CV_NOT_FOUND_MESSAGE;
		}
		return Constants.MALFORMED_REQUEST_MESSAGE;
	}

	public static String errorMessageDelete(CVRequest request, CVRepository repo) {
		if (RequestChecker.isValidDelete(request)) {
			return Constants.CV_NOT_FOUND_MESSAGE;
		}
		return Constants.MALFORMED_REQUEST_MESSAGE;
	}
	
	public static String errorMessageDelete(UserRequest request, UserRepository repo) {
		if (RequestChecker.userExists(request, repo)) {
			return Constants.USER_NOT_FOUND_MESSAGE;
		}
		return Constants.MALFORMED_REQUEST_MESSAGE;
	}

	public static Iterable<CV> multiCVError(UserRequest request) {
		ArrayList<CV> errorList = new ArrayList<>();
		errorList.add(singleCVError(request));
		return errorList;
	}

	public static CV singleCVError(UserRequest request) {
		CV errorMessage = new CV();
		errorMessage.setErrorMessage(errorMessage(request));
		return errorMessage;
	}

}