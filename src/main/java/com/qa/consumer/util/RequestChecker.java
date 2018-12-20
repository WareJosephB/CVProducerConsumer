package com.qa.consumer.util;

import com.qa.consumer.persistence.repository.CVRepository;
import com.qa.consumer.persistence.repository.TraineeRepository;
import com.qa.persistence.domain.CV;
import com.qa.persistence.domain.CVRequest;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.Trainer;
import com.qa.persistence.domain.TrainingManager;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestChecker {
	
	private RequestChecker() {
		
	}

	public static boolean isValid(UserRequest request) {
		return !(request.getUserToAddOrUpdate() == null || request.getUsername() == null);
	}

	public static boolean isValidDelete(UserRequest request) {
		return !(request.getUserToAddOrUpdate() == null || request.getUsername() == null);
	}

	public static boolean userExists(UserRequest request, TraineeRepository repo) {
		return repo.findById(request.getUsername()).isPresent();
	}

	public static boolean isValid(CVRequest request) {
		return (request.getCv() != (null));
	}

	public static boolean isValidDelete(CVRequest request) {
		return (0 != request.getcvIDtoActUpon());
	}

	public static boolean cvExists(CVRequest request, CVRepository repo) {
		return repo.findById(request.getcvIDtoActUpon()).isPresent();
	}

	public static boolean validSearch(CVRequest request) {
		return (request.getSearchString() != null);
	}

	public static Iterable<Trainer> multiTrainerError(UserRequest request) {
		ArrayList<Trainer> errorList = new ArrayList<>();
		errorList.add(singleTrainerError(request).get());
		return errorList;
	}

	public static Optional<Trainer> singleTrainerError(UserRequest request) {
		Trainer errorMessage = new Trainer();
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
		CV errorMessage = new CV(error);
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

	public static String errorMessageDelete(UserRequest request, TraineeRepository repo) {
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
		return new CV(errorMessage(request));
	}

	public static boolean tagged(CVRequest request, TraineeRepository repo) {
		String authorname = request.getCv().getAuthorName();
		Trainee author = repo.findById(authorname).get();
		return !(author.getEmails().isEmpty());
	}

	public static Iterable<Trainee> multiTraineeError(UserRequest request) {
		ArrayList<Trainee> errorList = new ArrayList<>();
		errorList.add(singleTraineeError(request).get());
		return errorList;
	}

	public static Optional<Trainee> singleTraineeError(UserRequest request) {
		Trainee errorMessage = new Trainee();
		errorMessage.setFirstName(errorMessage(request));
		return Optional.of(errorMessage);
	}

	public static Iterable<TrainingManager> multiTrainingManagerError(UserRequest request) {
		ArrayList<TrainingManager> errorList = new ArrayList<>();
		errorList.add(singleTrainingManagerError(request).get());
		return errorList;
	}

	public static Optional<TrainingManager> singleTrainingManagerError(UserRequest request) {
		TrainingManager errorMessage = new TrainingManager();
		errorMessage.setFirstName(errorMessage(request));
		return Optional.of(errorMessage);
	}

	public static Iterable<User> multiUserError(UserRequest request) {
		ArrayList<User> errorList = new ArrayList<>();
		errorList.add(singleUserError(request).get());
		return errorList;
	}

	public static Optional<User> singleUserError(UserRequest request) {
		User errorMessage = new Trainer();
		errorMessage.setFirstName(errorMessage(request));
		return Optional.of(errorMessage);
	}

	public static List<String> multiError(UserRequest request) {
		ArrayList<String> error = new ArrayList<String>();
		error.add(errorMessage(request));
		return error;
	}

}