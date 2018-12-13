package com.qa.consumer;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qa.consumer.persistence.repository.TrainerRepository;
import com.qa.consumer.service.TrainerService;
import com.qa.consumer.service.TrainingManagerService;
import com.qa.consumer.util.Constants;
import com.qa.persistence.domain.Trainer;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.UserRequest.requestType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrainerConsumerApplicationTests {

	@InjectMocks
	private TrainerService service;

	@Mock
	private TrainerRepository repo;

	@Mock
	private TrainingManagerService promoteService;

	private UserRequest goodRequest;
	private UserRequest badRequest;

	private Trainer rob;
	private Trainer bob;

	private ArrayList<User> trainers;

	@Before
	public void setUp() {
		goodRequest = new UserRequest();
		badRequest = new UserRequest();

		rob = new Trainer("a@b.com");
		bob = new Trainer("");

		trainers = new ArrayList<User>();
		trainers.add(rob);
		trainers.add(bob);
	}

	@Test
	public void testFindParse() {
		Mockito.when(repo.findById("a@b.com")).thenReturn(Optional.of(rob));
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());
		
		
		goodRequest.setHowToAct(requestType.READ);
		goodRequest.setUserToAddOrUpdate(rob);
		badRequest.setHowToAct(requestType.READ);

		assertEquals(Optional.of(rob), service.singleParse(goodRequest));
		assertEquals(service.singleError().toString(), service.singleParse(badRequest).toString());

		badRequest.setUserToAddOrUpdate(bob);

		assertEquals(Optional.empty(), service.singleParse(badRequest));

	}

	@Test
	public void testDeleteParse() {
		Mockito.when(repo.findById("a@b.com")).thenReturn(Optional.of(rob));
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());

		goodRequest.setHowToAct(requestType.DELETE);
		goodRequest.setUserToAddOrUpdate(rob);
		badRequest.setHowToAct(requestType.DELETE);

		assertEquals(Constants.USER_DELETED_MESSAGE, service.messageParse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.messageParse(badRequest));

		badRequest.setUserToAddOrUpdate(bob);
		assertEquals(Constants.USER_NOT_FOUND_MESSAGE, service.messageParse(badRequest));

	}

	@Test
	public void testUpdateParse() {
		Mockito.when(repo.findById("a@b.com")).thenReturn(Optional.of(rob));
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());

		goodRequest.setHowToAct(requestType.UPDATE);
		goodRequest.setUserToAddOrUpdate(rob);
		badRequest.setHowToAct(requestType.UPDATE);

		assertEquals(Constants.USER_UPDATED_MESSAGE, service.messageParse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.messageParse(badRequest));

		badRequest.setUserToAddOrUpdate(bob);

		assertEquals(Constants.USER_NOT_FOUND_MESSAGE, service.messageParse(badRequest));
	}

	@Test
	public void testAddParse() {
		goodRequest.setHowToAct(requestType.CREATE);
		goodRequest.setUserToAddOrUpdate(rob);
		badRequest.setHowToAct(requestType.CREATE);

		assertEquals(Constants.USER_ADDED_MESSAGE, service.messageParse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.messageParse(badRequest));

	}

	@Test
	public void testFindAllAndMalformedParse() {
		Mockito.when(repo.findAll()).thenReturn(trainers);
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());

		goodRequest.setHowToAct(requestType.READALL);

		assertEquals(trainers, service.multiParse(goodRequest));
		assertEquals(service.multiError().toString(), service.multiParse(badRequest).toString());
	}

	@Test
	public void testPromoteParse() {
		Mockito.when(repo.findById("a@b.com")).thenReturn(Optional.of(rob));
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());
		Mockito.when(promoteService.add(rob)).thenReturn(Constants.USER_ADDED_MESSAGE);

		goodRequest.setHowToAct(requestType.PROMOTE);
		goodRequest.setUserToAddOrUpdate(rob);
		badRequest.setHowToAct(requestType.PROMOTE);

		assertEquals(Constants.USER_PROMOTED_MESSAGE, service.messageParse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.messageParse(badRequest));
	}

}