package com.qa.consumer.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.repository.TraineeRepository;
import com.qa.consumer.util.Constants;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.Trainer;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.UserRequest.requestType;

@Component
public class TraineeService implements UserServicable<Trainee> {

	@Autowired
	private TraineeRepository repo;

	@Autowired
	private TrainerService promoteService;

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
		if (request.getUserToAddOrUpdate() == null || request.getUserToAddOrUpdate().getUsername() == null) {
			return singleError();
		} else {
			return get(request.getUserToAddOrUpdate().getUsername());
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
		if (request.getUserToAddOrUpdate() == null) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		} else {
			repo.save((Trainee) request.getUserToAddOrUpdate());
			return Constants.USER_ADDED_MESSAGE;
		}
	}

	@Override
	public User add(Trainee user) {
		return repo.save(user);
	}

	@Override
	public String update(UserRequest request) {
		if (request.getUserToAddOrUpdate() == null || request.getUserToAddOrUpdate().getUsername() == null) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}
		Optional<User> userToUpdate = get(request.getUserToAddOrUpdate().getUsername());
		Trainee updatedUser = (Trainee) request.getUserToAddOrUpdate();
		if (!userToUpdate.isPresent()) {
			return Constants.USER_NOT_FOUND_MESSAGE;
		} else {
			update((Trainee) userToUpdate.get(), updatedUser);
			return Constants.USER_UPDATED_MESSAGE;
		}
	}

	@Override
	public void update(Trainee userToUpdate, Trainee updatedUser) {
		userToUpdate.setFirstName(updatedUser.getFirstName());
		userToUpdate.setLastName(updatedUser.getLastName());
	}

	@Override
	public String delete(UserRequest request) {
		if (request.getUserToAddOrUpdate() == null || request.getUserToAddOrUpdate().getUsername() == null) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		} else {
			Optional<User> userToDelete = get(request.getUserToAddOrUpdate().getUsername());
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
		if (request.getUserToAddOrUpdate() == null || request.getUserToAddOrUpdate().getUsername() == null) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}
		String promotedEmail = request.getUserToAddOrUpdate().getUsername();
		if (repo.findById(promotedEmail).isPresent()) {
			Trainee traineeToPromote = (Trainee) repo.findById(promotedEmail).get();
			Trainer promotedTrainee = new Trainer(traineeToPromote);
			repo.deleteById(promotedEmail);
			request.setUserToAddOrUpdate(promotedTrainee);
			promoteService.add(request);
			return Constants.USER_PROMOTED_MESSAGE;
		}
		return Constants.MALFORMED_REQUEST_MESSAGE;
	}

	@Override
	public Iterable<User> multiError() {
		ArrayList<User> errorList = new ArrayList<>();
		User errorMessage = new Trainee();
		errorMessage.setFirstName(Constants.MALFORMED_REQUEST_MESSAGE);
		errorList.add(errorMessage);
		return errorList;
	}

	@Override
	public Optional<User> singleError() {
		User errorMessage = new Trainee();
		errorMessage.setFirstName(Constants.MALFORMED_REQUEST_MESSAGE);
		return Optional.of(errorMessage);
	}

}
