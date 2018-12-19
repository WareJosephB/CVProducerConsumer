package com.qa.consumer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.repository.TraineeRepository;
import com.qa.persistence.domain.CVRequest;
import com.qa.persistence.domain.Trainee;

@Component
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired 
	private TraineeRepository repo;

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}
	
	public void email(CVRequest request) {
		request.getCv().getAuthorName();
		Trainee trainee = (Trainee) repo.findById(request.getCv().getAuthorName()).get();
		for (String e : trainee.getEmails()) {
			sendSimpleMessage(e, "enum", "text");
			
		}
		 
		
	}
}
