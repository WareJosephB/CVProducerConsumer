package com.qa.consumer.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.service.TrainerService;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;

@Component
public class TrainerRequestReceiver {

	@Autowired
	private TrainerService trainerService;

	@JmsListener(destination = Constants.INCOMING_TRAINER_QUEUE_NAME, containerFactory = "myFactory")
	public Optional<User> returnTrainerQueue(UserRequest request) {
		return trainerService.singleParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINER_QUEUE_NAME, containerFactory = "myFactory")
	public Iterable<User> returnTrainersQueue(UserRequest request) {
		return trainerService.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINER_QUEUE_NAME, containerFactory = "myFactory")
	public String returnTrainerMessageQueue(UserRequest request) {
		return trainerService.messageParse(request);
	}
}
