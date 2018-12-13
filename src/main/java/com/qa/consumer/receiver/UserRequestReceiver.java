package com.qa.consumer.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.service.AllUserService;
import com.qa.consumer.util.Constants;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;

@Component
public class UserRequestReceiver {

	@Autowired
	private AllUserService userService;

	@JmsListener(destination = Constants.INCOMING_ALLUSER_QUEUE_NAME, containerFactory = Constants.FACTORY_NAME)
	public Iterable<User> returnUsersQueue(UserRequest request) {
		return userService.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_ALLUSER_QUEUE_NAME, containerFactory = Constants.FACTORY_NAME)
	public String returnUserMessageQueue(UserRequest request) {
		return userService.messageParse(request);
	}

}