package com.qa.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.domain.UserRequest;
import com.qa.consumer.persistence.domain.UserRequest.requestType;
import com.qa.consumer.persistence.repository.AllUsersRepository;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.UserProducer;

@Component
public class AllUserService {

	@Autowired
	private AllUsersRepository repo;

	@Autowired
	private UserProducer producer;

	public String parse(UserRequest request) {
		if (request.getHowToAct() == requestType.DELETEALL) {
			return deleteAll();
		} else if (request.getHowToAct() == requestType.READALL) {
			return getAll();
		}
		return Constants.MALFORMED_REQUEST_MESSAGE;
	}

	public String getAll() {
		return producer.produce(repo.findAll(), Constants.OUTGOING_ALLUSER_QUEUE_NAME);
	}

	public String deleteAll() {
		repo.deleteAll();
		return Constants.USER_ALL_DELETED_MESSAGE;
	}

}
