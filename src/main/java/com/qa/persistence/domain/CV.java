package com.qa.persistence.domain;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

@Document
public class CV {

	@Field("_id")
	private long cvid;
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

}