package com.qa.consumer.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.repository.TrainingManagerRepository;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.RequestChecker;
import com.qa.persistence.domain.Trainer;
import com.qa.persistence.domain.TrainingManager;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.UserRequest.requestType;

@Component
public class TrainingManagerService implements UserServicable<TrainingManager> {

	@Autowired
	private TrainingManagerRepository repo;

	@Override
	public Iterable<User> multiParse(UserRequest request) {
		if (request.getHowToAct() == requestType.READALL) {
			return getAll();
		}
		return multiError();
	}

	@Override
	public Iterable<User> getAll() {
		return repo.findAll();
	}

	@Override
	public Optional<User> singleParse(UserRequest request) {
		if (request.getHowToAct() == requestType.READ) {
			return get(request);
		}
		return singleError();
	}

	@Override
	public Optional<User> get(UserRequest request) {
		if (RequestChecker.isInvalid(request)) {
			return singleError();
		} else {
			return get(request.getUsername());
		}
	}

	@Override
	public Optional<User> get(String userName) {
		return repo.findById(userName);
	}

	@Override
	public String messageParse(UserRequest request) {
		requestType type = request.getHowToAct();
		switch (type) {
		case CREATE:
			return add(request);
		case UPDATE:
			return update(request);
		case DELETE:
			return delete(request);
		case PROMOTE:
			return promote(request);
		default:
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}
	}

	@Override
	public String add(UserRequest request) {
		if (RequestChecker.isInvalid(request)) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		} else {
			repo.save((TrainingManager) request.getUserToAddOrUpdate());
			return Constants.USER_ADDED_MESSAGE;
		}
	}

	@Override
	public User add(TrainingManager user) {
		return repo.save(user);
	}

	public String add(Trainer user) {
		repo.save(new TrainingManager(user));
		return Constants.USER_ADDED_MESSAGE;
	}

	@Override
	public String update(UserRequest request) {
		if (RequestChecker.isInvalid(request)) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}
		Optional<User> userToUpdate = get(request);
		TrainingManager updatedUser = (TrainingManager) request.getUserToAddOrUpdate();
		if (!userToUpdate.isPresent()) {
			return Constants.USER_NOT_FOUND_MESSAGE;
		} else {
			update((TrainingManager) userToUpdate.get(), updatedUser);
			return Constants.USER_UPDATED_MESSAGE;
		}
	}

	@Override
	public void update(TrainingManager userToUpdate, TrainingManager updatedUser) {
		userToUpdate.setFirstName(updatedUser.getFirstName());
		userToUpdate.setLastName(updatedUser.getLastName());
	}

	@Override
	public String delete(UserRequest request) {
		if (RequestChecker.isInvalid(request)) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		} else {
			Optional<User> userToDelete = get(request);
			if (!userToDelete.isPresent()) {
				return Constants.USER_NOT_FOUND_MESSAGE;
			} else {
				delete(userToDelete.get().getUsername());
				return Constants.USER_DELETED_MESSAGE;
			}
		}
	}

	@Override
	public void delete(String userName) {
		repo.deleteById(userName);
	}

	@Override
	public String promote(UserRequest request) {
		return Constants.MALFORMED_REQUEST_MESSAGE;
	}

	@Override
	public Iterable<User> multiError() {
		ArrayList<User> errorList = new ArrayList<>();
		User errorMessage = new TrainingManager();
		errorMessage.setFirstName(Constants.MALFORMED_REQUEST_MESSAGE);
		errorList.add(errorMessage);
		return errorList;
	}

	@Override
	public Optional<User> singleError() {
		User errorMessage = new TrainingManager();
		errorMessage.setFirstName(Constants.MALFORMED_REQUEST_MESSAGE);
		return Optional.of(errorMessage);
	}

}
