package com.qa.persistence.domain;

public class Trainer extends User {

	public Trainer(Trainee trainee) {
		this.setFirstName(trainee.getFirstName());
		this.setLastName(trainee.getLastName());
		this.setUsername(trainee.getUsername());
	}

	public Trainer(String username) {
		super(username);
		this.type = "Trainer";
	}

	public Trainer() {
		super();
		this.type = "Trainer";
	}

}
