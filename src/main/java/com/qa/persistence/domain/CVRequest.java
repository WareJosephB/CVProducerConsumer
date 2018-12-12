package com.qa.persistence.domain;

public class CVRequest {

	public enum requestType {
		CREATE, READ, UPDATE, DELETE, READALL, SEARCH
	}

	private CV cv;
	private requestType type;
	private long cvIDtoActUpon;
	private String searchString;

	public CV getCv() {
		return cv;
	}

	public void setCv(CV cv) {
		this.cv = cv;
	}

	public requestType getType() {
		return type;
	}

	public void setType(requestType type) {
		this.type = type;
	}

	public long getcvIDtoActUpon() {
		return cvIDtoActUpon;
	}

	public void setcvIDtoActUpon(long cvID) {
		this.cvIDtoActUpon = cvID;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

}