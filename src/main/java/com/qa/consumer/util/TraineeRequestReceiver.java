package com.qa.consumer.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.service.TraineeService;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;

@Component
public class TraineeRequestReceiver {

	@Autowired
	private TraineeService traineeService;

	@JmsListener(destination = Constants.INCOMING_TRAINEE_QUEUE_NAME, containerFactory = "myFactory")
	public Optional<User> returnTraineeQueue(UserRequest request) {
		return traineeService.singleParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINEE_QUEUE_NAME, containerFactory = "myFactory")
	public Iterable<User> returnTraineesQueue(UserRequest request) {
		return traineeService.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINEE_QUEUE_NAME, containerFactory = "myFactory")
	public String returnTraineeMessageQueue(UserRequest request) {
		return traineeService.messageParse(request);
	}
}
