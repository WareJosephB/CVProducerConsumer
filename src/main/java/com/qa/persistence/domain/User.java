package com.qa.persistence.domain;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", defaultImpl = Object.class)
@JsonSubTypes({ @JsonSubTypes.Type(value = Trainee.class, name = "Trainee"),
				@JsonSubTypes.Type(value = Trainer.class, name = "Trainer"),
				@JsonSubTypes.Type(value = TrainingManager.class, name = "TrainingManager") })
public abstract class User {

	private String firstName;
	private String lastName;
	@Field("_id")
	private String userName;
	private String password;

	@JsonTypeId
	public String type;

	public User(String userName) {
		this.setUsername(userName);
	}

	public User() {

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String username) {
		this.userName = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
