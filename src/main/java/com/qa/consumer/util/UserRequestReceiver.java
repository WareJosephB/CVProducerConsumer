package com.qa.consumer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.service.AllUserService;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;

@Component
public class UserRequestReceiver {

	@Autowired
	private AllUserService userService;

	@JmsListener(destination = Constants.INCOMING_ALLUSER_QUEUE_NAME, containerFactory = "myFactory")
	public Iterable<User> returnUsersQueue(UserRequest request) {
		return userService.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_ALLUSER_QUEUE_NAME, containerFactory = "myFactory")
	public String returnUserMessageQueue(UserRequest request) {
		return userService.messageParse(request);
	}

}