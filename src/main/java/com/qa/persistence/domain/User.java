package com.qa.persistence.domain;


import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@Document
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", defaultImpl = Object.class)
@JsonSubTypes({ @JsonSubTypes.Type(value = Trainee.class, name = "Trainee"),
		@JsonSubTypes.Type(value = Trainer.class, name = "Trainer"),
		@JsonSubTypes.Type(value = TrainingManager.class, name = "TrainingManager") })
public abstract class User {

	private String firstName;
	private String lastName;
	@Field("_id")
	private String username;

	@JsonTypeId
	public String type;

	public User(String username) {
		this.setUsername(username);
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
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String toString() {
		return "[" + this.username + ": " + this.lastName + ", " + this.firstName + "]";
	}

}
