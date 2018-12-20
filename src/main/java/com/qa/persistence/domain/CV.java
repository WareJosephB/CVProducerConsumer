package com.qa.persistence.domain;

import org.springframework.data.mongodb.core.mapping.Field;

public class CV {

	@Field("_id")
	private Long _id;
	private byte[] contents;
	private String fileName;
	private String authorName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	public CV(String string, byte[] contents, String traineeUsername) {
		this.fileName = string;
		this.contents = contents;
		this.authorName = traineeUsername;
	}

	public CV() {
	}

	public CV(String fileNameOrError) {
		this.setFileName(fileNameOrError);
	}

	public Long getCvID() {
		return _id;
	}

	public void setCvID(Long cvID) {
		this._id = cvID;
	}

}
