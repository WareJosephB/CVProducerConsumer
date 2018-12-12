package com.qa.consumer.util;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;

import com.qa.persistence.domain.User;

@Component
public class UserProducer<T extends User> {

	@Autowired
	private JmsTemplate jmsTemplate;

	public String produce(T user, String queueName) {
		jmsTemplate.convertAndSend(queueName, user);
		return Constants.USER_QUEUED_MESSAGE;
	}

	public String produce(Iterable<User> users, String queueName) {
		jmsTemplate.convertAndSend(Constants.INCOMING_CV_QUEUE_NAME, users);
		return Constants.USERS_QUEUED_MESSAGE;

	}
}
