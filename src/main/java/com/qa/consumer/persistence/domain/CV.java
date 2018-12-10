package com.qa.consumer.persistence.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

@Document
public class CV {

	@Id
	private long _id;
	@TextIndexed
	private MultipartFile cvFile;

	public MultipartFile getCV() {
		return cvFile;
	}

	public void setCV(MultipartFile cv) {
		cvFile = cv;
	}

}