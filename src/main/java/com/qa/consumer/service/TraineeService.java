package com.qa.consumer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.repository.TraineeRepository;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.UserProducer;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.UserRequest.requestType;

@Component
public class TraineeService implements UserServicable<Trainee> {

	@Autowired
	private TraineeRepository repo;

	@Autowired
	private UserProducer<Trainee> producer;

	@Autowired
	private TrainerService promoteService;

	@Override
	public String parse(UserRequest request) {
		if (request.getHowToAct() == requestType.CREATE) {
			return add(request);
		} else if (request.getHowToAct() == requestType.UPDATE) {
			return update(request);
		} else if (request.getHowToAct() == requestType.DELETE) {
			return delete(request);
		} else if (request.getHowToAct() == requestType.READ) {
			return get(request);
		} else if (request.getHowToAct() == requestType.READALL) {
			return send(getAll());
		} else if (request.getHowToAct() == requestType.PROMOTE) {
			return promote(request);
		} else if (request.getHowToAct() == requestType.DELETEALL) {
			return deleteAll();
		}
		return Constants.MALFORMED_REQUEST_MESSAGE;

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
	public String get(UserRequest request) {
		if (request.getUserToAddOrUpdate() == null || request.getUserToAddOrUpdate().getUsername() == null) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		} else {
			return send(request.getUserToAddOrUpdate().getUsername());

		}
	}

	@Override
	public Iterable<User> getAll() {
		return repo.findAll();
	}

	@Override
	public String promote(UserRequest request) {
		if (request.getUserToAddOrUpdate() == null || request.getUserToAddOrUpdate().getUsername() == null) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}
		String promotedEmail = request.getUserToAddOrUpdate().getUsername();
		if (repo.findById(promotedEmail).isPresent()) {
			Trainee promotedTrainee = (Trainee) repo.findById(promotedEmail).get();
			repo.deleteById(promotedEmail);
			request.setUserToAddOrUpdate(promotedTrainee);
			promoteService.add(request);
			return Constants.USER_PROMOTED_MESSAGE;
		}
		return Constants.MALFORMED_REQUEST_MESSAGE;

	}

	@Override
	public String deleteAll() {
		repo.deleteAll();
		return Constants.USER_ALL_DELETED_MESSAGE;
	}

	public String send(Iterable<User> trainees) {
		return producer.produce(trainees, Constants.OUTGOING_TRAINEE_QUEUE_NAME);
	}

	public String send(Optional<User> trainee) {
		if (trainee.isPresent()) {
			return producer.produce((Trainee) trainee.get(), Constants.OUTGOING_TRAINEE_QUEUE_NAME);
		} else {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}

	}

	@Override
	public User add(Trainee user) {
		return repo.save(user);
	}

	@Override
	public void delete(String userName) {
		repo.deleteById(userName);
	}

	@Override
	public void update(Trainee userToUpdate, Trainee updatedUser) {
		userToUpdate.setFirstName(updatedUser.getFirstName());
		userToUpdate.setLastName(updatedUser.getLastName());

	}

	@Override
	public String send(String userName) {
		if (repo.findById(userName).isPresent()) {
			return producer.produce((Trainee) get(userName).get(), Constants.OUTGOING_TRAINEE_QUEUE_NAME);
		} else {
			return Constants.USER_NOT_FOUND_MESSAGE;
		}
	}

	@Override
	public Optional<User> get(String userName) {
		return repo.findById(userName);
	}

}
