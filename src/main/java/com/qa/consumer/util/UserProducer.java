package com.qa.consumer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.domain.User;

@Component
public class UserProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	public String produce(User user, String queueName) {
		jmsTemplate.convertAndSend(queueName, user);
		return Constants.USER_QUEUED_MESSAGE;
	}

	public String produce(Iterable<User> users, String queueName) {
		jmsTemplate.convertAndSend(queueName, users);
		return Constants.USERS_QUEUED_MESSAGE;
	}
}
