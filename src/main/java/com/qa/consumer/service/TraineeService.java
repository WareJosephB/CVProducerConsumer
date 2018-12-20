package com.qa.consumer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qa.consumer.persistence.repository.CVRepository;
import com.qa.consumer.persistence.repository.TraineeRepository;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.RequestChecker;
import com.qa.persistence.domain.CV;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.Trainer;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.UserRequest.requestType;

@Component
@Transactional
public class TraineeService implements PromotableUserServicable<Trainee> {

	@Autowired
	private TraineeRepository repo;

	@Autowired
	private CVRepository cvRepo;

	@Autowired
	private TrainerService promoteService;

	public Optional<Trainee> get(String userName) {
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
		case TAG:
			return tag(request);
		default:
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}
	}

	public List<String> multiParse(UserRequest request) {
		requestType type = request.getHowToAct();
		if (type == requestType.READALL) {
			return listCVs(request);
		}
		return RequestChecker.multiError(request);
	}

	private ArrayList<String> listCVs(UserRequest request) {
		if (RequestChecker.isValid(request)) {
			if (RequestChecker.userExists(request, repo)) {
				return listCVs(request.getUsername());
			}
			ArrayList<String> errorMessage = new ArrayList<>();
			errorMessage.add(Constants.USER_NOT_FOUND_MESSAGE);
			return errorMessage;
		}
		ArrayList<String> errorMessage = new ArrayList<>();
		errorMessage.add(RequestChecker.errorMessage(request));
		return errorMessage;
	}

	private ArrayList<String> listCVs(String username) {
		ArrayList<String> results = new ArrayList<>();
		for (CV cv : cvRepo.findAllByAuthorName(username)) {
			results.add(cv.getCvID().toString());
		}
		return results;
	}

	private String tag(UserRequest request) {
		if (RequestChecker.isValid(request)) {
			if (RequestChecker.userExists(request, repo)) {
				tag(request.getUsername(), ((Trainee) request.getUserToAddOrUpdate()).getEmails().get(0));
				return Constants.USER_UPDATED_MESSAGE;
			}
			return Constants.USER_NOT_FOUND_MESSAGE;
		}
		return RequestChecker.errorMessage(request);
	}

	private String tag(String username, String emailaddress) {
		Trainee thisTrainee = repo.findById(username).get();
		if (thisTrainee.getEmails().contains(emailaddress)) {
			thisTrainee.getEmails().remove(emailaddress);
			repo.deleteById(username);
			repo.save(thisTrainee);
			return Constants.TAG_REMOVED_MESSAGE;
		} else {
			thisTrainee.getEmails().add(emailaddress);
			repo.deleteById(username);
			repo.save(thisTrainee);
			return Constants.TAG_ADDED_MESSAGE;
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

	public Trainee add(Trainee user) {
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
	public void update(String username, User updatedUser) {
		Trainee userToUpdate = (Trainee) get(username).get();
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