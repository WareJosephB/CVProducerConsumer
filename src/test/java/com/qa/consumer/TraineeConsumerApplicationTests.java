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

import com.qa.consumer.persistence.repository.TraineeRepository;
import com.qa.consumer.service.TraineeService;
import com.qa.consumer.service.TrainerService;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.RequestChecker;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.UserRequest.requestType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TraineeConsumerApplicationTests {

	@InjectMocks
	private TraineeService service;

	@Mock
	private TraineeRepository repo;

	@Mock
	private TrainerService promoteService;

	private UserRequest goodRequest;
	private UserRequest badRequest;

	private Trainee rob;
	private Trainee bob;

	private ArrayList<User> trainees;

	@Before
	public void setUp() {
		goodRequest = new UserRequest();
		badRequest = new UserRequest();

		rob = new Trainee("a@b.com");
		bob = new Trainee("");

		trainees = new ArrayList<User>();
		trainees.add(rob);
		trainees.add(bob);
	}

	@Test
	public void testFindParse() {
		Mockito.when(repo.findById("a@b.com")).thenReturn(Optional.of(rob));
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());
		

		goodRequest.setHowToAct(requestType.READ);
		goodRequest.setUserToAddOrUpdate(rob);
		badRequest.setHowToAct(requestType.READ);

		assertEquals(Optional.of(rob), service.singleParse(goodRequest));
		assertEquals(RequestChecker.singleError(badRequest).toString(), service.singleParse(badRequest).toString());

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

		assertEquals(RequestChecker.singleError(badRequest).toString(), service.singleParse(badRequest).toString());
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

		assertEquals(RequestChecker.singleError(badRequest).toString(), service.singleParse(badRequest).toString());
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
		Mockito.when(repo.findAll()).thenReturn(trainees);
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());

		goodRequest.setHowToAct(requestType.READALL);

		assertEquals(trainees, service.multiParse(goodRequest));
		assertEquals(RequestChecker.multiError(badRequest).toString(), service.multiParse(badRequest).toString());
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