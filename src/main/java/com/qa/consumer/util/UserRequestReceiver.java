package com.qa.consumer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.domain.UserRequest;
import com.qa.consumer.service.AllUserService;
import com.qa.consumer.service.TraineeService;
import com.qa.consumer.service.TrainerService;
import com.qa.consumer.service.TrainingManagerService;

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
	public void allUserReceiveMessage(UserRequest request) {
		userService.parse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINEE_QUEUE_NAME, containerFactory = "myFactory")
	public void traineeReceiveMessage(UserRequest request) {
		traineeService.parse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINER_QUEUE_NAME, containerFactory = "myFactory")
	public void trainerReceiveMessage(UserRequest request) {
		trainerService.parse(request);
	}

	@JmsListener(destination = Constants.INCOMING_TRAINING_MANAGER_QUEUE_NAME, containerFactory = "myFactory")
	public void trainingManagerReceiveMessage(UserRequest request) {
		trainingManagerService.parse(request);
	}

}