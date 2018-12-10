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

import com.qa.consumer.persistence.domain.TrainingManager;
import com.qa.consumer.persistence.domain.User;
import com.qa.consumer.persistence.domain.UserRequest;
import com.qa.consumer.persistence.domain.UserRequest.requestType;
import com.qa.consumer.persistence.repository.TrainingManagerRepository;
import com.qa.consumer.service.TrainingManagerService;
import com.qa.consumer.service.TrainingManagerService;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.UserProducer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrainingManagerConsumerApplicationTests {

	@InjectMocks
	private TrainingManagerService service;

	@Mock
	private TrainingManagerRepository repo;

	@Mock
	private UserProducer producer;

	private UserRequest goodRequest;
	private UserRequest badRequest;

	private TrainingManager rob;
	private TrainingManager bob;

	private ArrayList<User> trainingManagers;

	@Before
	public void setUp() {
		goodRequest = new UserRequest();
		badRequest = new UserRequest();

		rob = new TrainingManager();
		rob.setUsername("a@b.com");
		bob = new TrainingManager();
		bob.setUsername("");

		trainingManagers = new ArrayList<User>();
		trainingManagers.add(rob);
		trainingManagers.add(bob);

	}

	@Test
	public void testFindParse() {
		Mockito.when(repo.findById("a@b.com")).thenReturn(Optional.of(rob));
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());
		Mockito.when(producer.produce(rob, Constants.OUTGOING_TRAINEE_QUEUE_NAME))
				.thenReturn(Constants.USER_QUEUED_MESSAGE);

		goodRequest.setHowToAct(requestType.READ);
		goodRequest.setUserToAddOrUpdate(rob);
		badRequest.setHowToAct(requestType.READ);

		assertEquals(Constants.USER_QUEUED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.parse(badRequest));

		badRequest.setUserToAddOrUpdate(bob);

		assertEquals(Constants.USER_NOT_FOUND_MESSAGE, service.parse(badRequest));

	}

	@Test
	public void testDeleteParse() {
		Mockito.when(repo.findById("a@b.com")).thenReturn(Optional.of(rob));
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());

		goodRequest.setHowToAct(requestType.DELETE);
		goodRequest.setUserToAddOrUpdate(rob);
		badRequest.setHowToAct(requestType.DELETE);

		assertEquals(Constants.USER_DELETED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.parse(badRequest));

		badRequest.setUserToAddOrUpdate(bob);
		assertEquals(Constants.USER_NOT_FOUND_MESSAGE, service.parse(badRequest));

	}

	@Test
	public void testUpdateParse() {
		Mockito.when(repo.findById("a@b.com")).thenReturn(Optional.of(rob));
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());

		goodRequest.setHowToAct(requestType.UPDATE);
		goodRequest.setUserToAddOrUpdate(rob);
		badRequest.setHowToAct(requestType.UPDATE);

		assertEquals(Constants.USER_UPDATED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.parse(badRequest));

		badRequest.setUserToAddOrUpdate(bob);

		assertEquals(Constants.USER_NOT_FOUND_MESSAGE, service.parse(badRequest));
	}

	@Test
	public void testAddParse() {
		goodRequest.setHowToAct(requestType.CREATE);
		goodRequest.setUserToAddOrUpdate(rob);
		badRequest.setHowToAct(requestType.CREATE);

		assertEquals(Constants.USER_ADDED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.parse(badRequest));

	}

	@Test
	public void testFindAllAndMalformedParse() {
		Mockito.when(repo.findAll()).thenReturn(trainingManagers);
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());
		Mockito.when(producer.produce(trainingManagers, Constants.OUTGOING_TRAINEE_QUEUE_NAME))
				.thenReturn(Constants.USERS_QUEUED_MESSAGE);

		goodRequest.setHowToAct(requestType.READALL);

		assertEquals(Constants.USERS_QUEUED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.parse(badRequest));
	}

	@Test
	public void testPromoteParse() {
		Mockito.when(repo.findById("a@b.com")).thenReturn(Optional.of(rob));
		Mockito.when(repo.findById("")).thenReturn(Optional.empty());

		goodRequest.setHowToAct(requestType.PROMOTE);
		goodRequest.setUserToAddOrUpdate(rob);
		badRequest.setHowToAct(requestType.PROMOTE);

		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.parse(badRequest));
	}

}