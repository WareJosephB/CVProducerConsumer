package com.qa.consumer.util;

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
	public String returnUsersQueue(UserRequest request) {
		return userService.messageParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINEE_QUEUE_NAME, containerFactory = "myFactory")
	public Trainee returnTraineeQueue(UserRequest request) {
		return traineeService.singleParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINEE_QUEUE_NAME, containerFactory = "myFactory")
	public Iterable<Trainee> returnTraineesQueue(UserRequest request) {
		return traineeService.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINER_QUEUE_NAME, containerFactory = "myFactory")
	public Trainer returnTrainerQueue(UserRequest request) {
		return trainerService.singleParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINER_QUEUE_NAME, containerFactory = "myFactory")
	public Iterable<Trainer> returnTrainersQueue(UserRequest request) {
		return trainerService.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINING_MANAGER_QUEUE_NAME, containerFactory = "myFactory")
	public TrainingManager returnTrainingManagerQueue(UserRequest request) {
		return trainingManagerService.singleParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINING_MANAGER_QUEUE_NAME, containerFactory = "myFactory")
	public Iterable<TrainingManager> returnTrainingManagersQueue(UserRequest request) {
		return trainingManagerService.multiParse(request);
	}

}