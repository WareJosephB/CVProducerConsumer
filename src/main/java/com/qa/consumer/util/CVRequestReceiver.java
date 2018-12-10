package com.qa.consumer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.domain.CVRequest;
import com.qa.consumer.service.CVService;

@Component
public class CVRequestReceiver {

	@Autowired
	private CVService service;

	@JmsListener(destination = Constants.INCOMING_CV_QUEUE_NAME, containerFactory = "myFactory")
	public void receiveMessage(CVRequest request) {
		service.parse(request);
	}

}