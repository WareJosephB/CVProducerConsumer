package com.qa.consumer.persistence.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("Trainee")
public class Trainee extends User {

	private boolean currentlyHired;
	private boolean flagged;

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

	public List<Optional<CV>> getCvList() {
		return cvList;
	}

	public void setCvList(List<Optional<CV>> cvList) {
		this.cvList = cvList;
	}

	private List<Optional<CV>> cvList;

}