package com.qa.persistence.domain;

public class TrainingManager extends User {

	public TrainingManager(Trainer trainer) {
		this.setFirstName(trainer.getFirstName());
		this.setLastName(trainer.getLastName());
		this.setUsername(trainer.getUsername());
		this.setPassword(trainer.getPassword());
	}

	public TrainingManager(String username) {
		super(username);
		this.type = "TrainingManager";
	}

	public TrainingManager() {
		super();
		this.type = "TrainingManager";
	}

}
