package com.qa.consumer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.repository.TraineeRepository;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.RequestChecker;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.Trainer;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.UserRequest.requestType;

@Component
public class TraineeService implements PromotableUserServicable<Trainee> {

	@Autowired
	private TraineeRepository repo;

	@Autowired
	private TrainerService promoteService;

	@Override
	public Iterable<User> multiParse(UserRequest request) {
		if (request.getHowToAct() == requestType.READALL) {
			return getAll();
		}
		return RequestChecker.multiError(request);
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
		return RequestChecker.singleError(request);
	}

	@Override
	public Optional<User> get(UserRequest request) {
		if (RequestChecker.isValid(request)) {
			return get(request.getUsername());
		}
		return RequestChecker.singleError(request);
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
		if (RequestChecker.isValid(request)) {
			repo.save((Trainee) request.getUserToAddOrUpdate());
			return Constants.USER_ADDED_MESSAGE;
		}
		return RequestChecker.errorMessage(request);
	}

	@Override
	public User add(User user) {
		return repo.save(user);
	}

	@Override
	public String update(UserRequest request) {
		if (RequestChecker.isValid(request)) {
			update(request.getUsername(), (Trainee) request.getUserToAddOrUpdate());
			return Constants.USER_UPDATED_MESSAGE;
		}
		return RequestChecker.errorMessage(request);
	}

	@Override
	public void update(String userName, User updatedUser) {
		Trainee userToUpdate = (Trainee) get(userName).get();
		userToUpdate.setFirstName(updatedUser.getFirstName());
		userToUpdate.setLastName(updatedUser.getLastName());
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

	@Override
	public String promote(UserRequest request) {
		if (RequestChecker.isValid(request)) {
			promote(request.getUsername());
			return Constants.USER_PROMOTED_MESSAGE;
		}
		return RequestChecker.errorMessage(request);
	}

	@Override
	public void promote(String username) {
		Trainee traineeToPromote = (Trainee) get(username).get();
		delete(username);
		promoteService.add(new Trainer(traineeToPromote));
	}

}