package com.qa.consumer.receiver;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.service.TrainerService;
import com.qa.consumer.util.Constants;
import com.qa.persistence.domain.Trainer;
import com.qa.persistence.domain.UserRequest;

@Component
public class TrainerRequestReceiver {

	@Autowired
	private TrainerService trainerService;

	@JmsListener(destination = Constants.INCOMING_TRAINER_QUEUE_NAME, containerFactory = Constants.FACTORY_NAME)
	public Optional<Trainer> returnTrainerQueue(UserRequest request) {
		return trainerService.singleParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINER_QUEUE_NAME, containerFactory = Constants.FACTORY_NAME)
	public Iterable<Trainer> returnTrainersQueue(UserRequest request) {
		return trainerService.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINER_QUEUE_NAME, containerFactory = Constants.FACTORY_NAME)
	public String returnTrainerMessageQueue(UserRequest request) {
		return trainerService.messageParse(request);
	}
}
