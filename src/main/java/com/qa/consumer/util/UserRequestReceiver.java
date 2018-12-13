package com.qa.consumer.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.service.AllUserService;
import com.qa.consumer.service.TraineeService;
import com.qa.consumer.service.TrainerService;
import com.qa.consumer.service.TrainingManagerService;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.Trainer;
import com.qa.persistence.domain.TrainingManager;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;

@Component
public class UserRequestReceiver {

	@Autowired
	private AllUserService userService;

	@Autowired
	private TraineeService traineeService;

	@Autowired
	private TrainerService trainerService;

	@Autowired
	private TrainingManagerService trainingManagerService;

	@JmsListener(destination = Constants.INCOMING_ALLUSER_QUEUE_NAME, containerFactory = "myFactory")
	public Iterable<User> returnUsersQueue(UserRequest request) {
		return userService.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_ALLUSER_QUEUE_NAME, containerFactory = "myFactory")
	public String returnUserMessageQueue(UserRequest request) {
		return userService.messageParse(request);
	}

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

	@JmsListener(destination = Constants.INCOMING_TRAINING_MANAGER_QUEUE_NAME, containerFactory = "myFactory")
	public Optional<User> returnTrainingManagerQueue(UserRequest request) {
		return trainingManagerService.singleParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINING_MANAGER_QUEUE_NAME, containerFactory = "myFactory")
	public Iterable<User> returnTrainingManagersQueue(UserRequest request) {
		return trainingManagerService.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINING_MANAGER_QUEUE_NAME, containerFactory = "myFactory")
	public String returnTrainingManagerStringQueue(UserRequest request) {
		return trainingManagerService.messageParse(request);
	}

}