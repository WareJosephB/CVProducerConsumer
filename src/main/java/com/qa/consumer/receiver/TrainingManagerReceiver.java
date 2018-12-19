package com.qa.consumer.receiver;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.service.TrainingManagerService;
import com.qa.consumer.util.Constants;
import com.qa.persistence.domain.TrainingManager;
import com.qa.persistence.domain.UserRequest;

@Component
public class TrainingManagerReceiver {
	@Autowired
	private TrainingManagerService trainingManagerService;

	@JmsListener(destination = Constants.INCOMING_TRAINING_MANAGER_QUEUE_NAME, containerFactory = Constants.FACTORY_NAME)
	public Optional<TrainingManager> returnTrainingManagerQueue(UserRequest request) {
		return trainingManagerService.singleParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINING_MANAGER_QUEUE_NAME, containerFactory = Constants.FACTORY_NAME)
	public Iterable<TrainingManager> returnTrainingManagersQueue(UserRequest request) {
		return trainingManagerService.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINING_MANAGER_QUEUE_NAME, containerFactory = Constants.FACTORY_NAME)
	public String returnTrainingManagerStringQueue(UserRequest request) {
		return trainingManagerService.messageParse(request);
	}

}
