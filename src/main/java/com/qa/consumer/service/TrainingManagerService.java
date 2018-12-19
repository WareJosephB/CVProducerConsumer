package com.qa.consumer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qa.consumer.persistence.repository.TrainingManagerRepository;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.RequestChecker;
import com.qa.persistence.domain.Trainer;
import com.qa.persistence.domain.TrainingManager;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.UserRequest.requestType;

@Component
@Transactional
public class TrainingManagerService implements UserServicable<TrainingManager> {

	@Autowired
	private TrainingManagerRepository repo;

	public Iterable<TrainingManager> multiParse(UserRequest request) {
		if (request.getHowToAct() == requestType.READALL) {
			return getAll();
		}
		return RequestChecker.multiTrainingManagerError(request);
	}

	public Iterable<TrainingManager> getAll() {
		return repo.findAll();
	}

	public Optional<TrainingManager> singleParse(UserRequest request) {
		if (request.getHowToAct() == requestType.READ) {
			return get(request);
		}
		return RequestChecker.singleTrainingManagerError(request);
	}

	public Optional<TrainingManager> get(UserRequest request) {
		if (RequestChecker.isValid(request)) {
			return get(request.getUsername());
		}
		return RequestChecker.singleTrainingManagerError(request);
	}

	public Optional<TrainingManager> get(String userName) {
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
		default:
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}
	}

	@Override
	public String add(UserRequest request) {
		if (RequestChecker.isValid(request)) {
			repo.save((TrainingManager) request.getUserToAddOrUpdate());
			return Constants.USER_ADDED_MESSAGE;
		}
		return RequestChecker.errorMessage(request);
	}

	public TrainingManager add(TrainingManager user) {
		return repo.save(user);
	}

	public String add(Trainer user) {
		repo.save(new TrainingManager(user));
		return Constants.USER_ADDED_MESSAGE;
	}

	@Override
	public String update(UserRequest request) {
		if (RequestChecker.isValid(request)) {
			update(request.getUsername(), (TrainingManager) request.getUserToAddOrUpdate());
			return Constants.USER_UPDATED_MESSAGE;
		}
		return RequestChecker.errorMessage(request);
	}

	@Override
	public void update(String userName, User updatedUser) {
		TrainingManager userToUpdate = (TrainingManager) get(userName).get();
		userToUpdate.setFirstName(updatedUser.getFirstName());
		userToUpdate.setLastName(updatedUser.getLastName());
		repo.save(userToUpdate);

	}

	@Override
	public String delete(UserRequest request) {
		if (RequestChecker.isValid(request)) {
			delete(request.getUsername());
			return Constants.USER_DELETED_MESSAGE;
		}
		return RequestChecker.errorMessage(request);
	}

	@Override
	public void delete(String userName) {
		repo.deleteById(userName);
	}

}