package com.qa.persistence.domain;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

@Document
public class CV {

	@Field("_id")
	private long cvid;

	public long getCvid() {
		return cvid;
	}

	public void setCvid(long cvid) {
		this.cvid = cvid;
	}

	public MultipartFile getCvFile() {
		return cvFile;
	}

	public void setCvFile(MultipartFile cvFile) {
		this.cvFile = cvFile;
	}

	@TextIndexed
	private MultipartFile cvFile;
	private String errorMessage;

	public MultipartFile getCV() {
		return cvFile;
	}

	public void setCV(MultipartFile cv) {
		cvFile = cv;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String toString() {
		return this.errorMessage;
	}

}