package com.qa.persistence.domain;

import javax.persistence.Id;

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
	@Id
	private String userName;

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

	public String toString() {
		return "[" + this.userName + ":" + this.lastName + ", " + this.firstName + "]";
	}

}
