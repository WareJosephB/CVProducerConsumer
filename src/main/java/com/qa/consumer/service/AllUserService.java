package com.qa.consumer.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.repository.AllUsersRepository;
import com.qa.consumer.util.Constants;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.UserRequest.requestType;

@Component
public class AllUserService {

	@Autowired
	private AllUsersRepository repo;

	public Iterable<User> getAll() {
		return repo.findAll();
	}

	public String deleteAll() {
		repo.deleteAll();
		return Constants.USER_ALL_DELETED_MESSAGE;
	}

	public Iterable<User> multiParse(UserRequest request) {
		if (request.getHowToAct() == requestType.READALL) {
			return getAll();
		}
		return error();

	}

	public String messageParse(UserRequest request) {
		if (request.getHowToAct() == requestType.DELETEALL) {
			return deleteAll();
		}
		return Constants.MALFORMED_REQUEST_MESSAGE;
	}

	private Iterable<User> error() {
		ArrayList<User> errorList = new ArrayList<>();
		User errorMessage = new Trainee();
		errorMessage.setFirstName(Constants.MALFORMED_REQUEST_MESSAGE);
		errorList.add(errorMessage);
		return errorList;
	}
}
