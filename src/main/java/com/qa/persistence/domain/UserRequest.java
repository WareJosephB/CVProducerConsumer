package com.qa.persistence.domain;

public class UserRequest {

	public enum requestType {
		CREATE, UPDATE, DELETE, READ, READALL, PROMOTE, DELETEALL
	}

	private requestType howToAct;
	private User userToAddOrUpdate;

	public requestType getHowToAct() {
		return howToAct;
	}

	public void setHowToAct(requestType howToAct) {
		this.howToAct = howToAct;
	}

	public User getUserToAddOrUpdate() {
		return userToAddOrUpdate;
	}

	public void setUserToAddOrUpdate(User userToAddOrUpdate) {
		this.userToAddOrUpdate = userToAddOrUpdate;
	}

}
