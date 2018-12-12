package com.qa.persistence.domain;

import java.util.List;

public class Trainee extends User {

	private boolean currentlyHired;
	private boolean flagged;
	private List<CV> cvList;

	public Trainee(String userName) {
		super(userName);
		this.type = "Trainee";
	}

	public Trainee() {
		this.type = "Trainee";
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

	public List<CV> getCvList() {
		return cvList;
	}

	public void setCvList(List<CV> cvList) {
		this.cvList = cvList;
	}

}