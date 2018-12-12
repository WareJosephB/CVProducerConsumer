package com.qa.consumer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.repository.TrainingManagerRepository;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.UserProducer;
import com.qa.persistence.domain.Trainer;
import com.qa.persistence.domain.TrainingManager;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.UserRequest.requestType;

@Component
public class TrainingManagerService implements UserServicable<TrainingManager> {

	@Autowired
	private TrainingManagerRepository repo;

	@Autowired
	private UserProducer<TrainingManager> producer;

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
			repo.save(request.getUserToAddOrUpdate());
			return Constants.USER_ADDED_MESSAGE;
		}
	}

	public String add(Trainer trainer) {
		TrainingManager promotedTrainer = new TrainingManager(trainer);
		repo.save(promotedTrainer);
		return Constants.USER_ADDED_MESSAGE;
	}

	@Override
	public String update(UserRequest request) {
		if (request.getUserToAddOrUpdate() == null || request.getUserToAddOrUpdate().getUsername() == null) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}
		Optional<User> userToUpdate = get(request.getUserToAddOrUpdate().getUsername());
		TrainingManager updatedUser = (TrainingManager) request.getUserToAddOrUpdate();
		if (!userToUpdate.isPresent()) {
			return Constants.USER_NOT_FOUND_MESSAGE;
		} else {
			update((TrainingManager) userToUpdate.get(), updatedUser);
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
		return Constants.MALFORMED_REQUEST_MESSAGE;

	}

	@Override
	public String deleteAll() {
		repo.deleteAll();
		return Constants.USER_ALL_DELETED_MESSAGE;
	}

	public String send(Iterable<User> trainingManagers) {
		return producer.produce(trainingManagers, Constants.OUTGOING_TRAINING_MANAGER_QUEUE_NAME);
	}

	public String send(Optional<User> trainingManager) {
		if (trainingManager.isPresent()) {
			return producer.produce((TrainingManager) trainingManager.get(),
					Constants.OUTGOING_TRAINING_MANAGER_QUEUE_NAME);
		} else {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}

	}

	@Override
	public User add(TrainingManager user) {
		return repo.save(user);
	}

	@Override
	public void delete(String userName) {
		repo.deleteById(userName);
	}

	@Override
	public void update(TrainingManager userToUpdate, TrainingManager updatedUser) {
		userToUpdate.setFirstName(updatedUser.getFirstName());
		userToUpdate.setLastName(updatedUser.getLastName());

	}

	@Override
	public String send(String userName) {
		if (repo.findById(userName).isPresent()) {
			return producer.produce((TrainingManager) get(userName).get(),
					Constants.OUTGOING_TRAINING_MANAGER_QUEUE_NAME);
		} else {
			return Constants.USER_NOT_FOUND_MESSAGE;
		}
	}

	@Override
	public Optional<User> get(String userName) {
		return repo.findById(userName);
	}

}
