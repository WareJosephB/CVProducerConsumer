package com.qa.consumer.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.service.CVService;
import com.qa.persistence.domain.CV;
import com.qa.persistence.domain.CVRequest;
import com.qa.persistence.domain.UserRequest;

@Component
public class CVRequestReceiver {

	@Autowired
	private CVService service;

	@JmsListener(destination = Constants.INCOMING_CV_QUEUE_NAME, containerFactory = "myFactory")
	public Iterable<CV> receiveCVs(CVRequest request) {
		return service.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_CV_QUEUE_NAME, containerFactory = "myFactory")
	public Iterable<CV> receiveAllCVsforUser(UserRequest request) {
		return service.multiParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_CV_QUEUE_NAME, containerFactory = "myFactory")
	public Optional<CV> receiveCV(CVRequest request) {
		return service.singleParse(request);
	}

	@JmsListener(destination = Constants.INCOMING_CV_QUEUE_NAME, containerFactory = "myFactory")
	public String receiveMessage(CVRequest request) {
		return service.messageParse(request);
	}

}