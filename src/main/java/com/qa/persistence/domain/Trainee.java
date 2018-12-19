package com.qa.persistence.domain;

import java.util.ArrayList;

public class Trainee extends User {

	private boolean currentlyHired;
	private boolean flagged;
	private ArrayList<String> emails;

	public Trainee(String userName) {
		super(userName);
		this.type = "Trainee";
		this.emails = new ArrayList<>();

	}

	public Trainee() {
		this.type = "Trainee";
		this.emails = new ArrayList<>();

	}

	public boolean isCurrentlyHired() {
		return currentlyHired;
	}

	public void setCurrentlyHired(boolean currentlyHired) {
		this.currentlyHired = currentlyHired;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}

	public ArrayList<String> getEmails() {
		return emails;
	}

	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
	}

}