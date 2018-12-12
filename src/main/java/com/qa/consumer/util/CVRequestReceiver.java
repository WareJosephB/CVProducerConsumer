package com.qa.consumer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.service.CVService;
import com.qa.persistence.domain.CVRequest;

@Component
public class CVRequestReceiver {

	@Autowired
	private CVService service;

	@JmsListener(destination = Constants.INCOMING_CV_QUEUE_NAME, containerFactory = "myFactory")
	public void receiveMessage(CVRequest request) {
		service.parse(request);
	}

}