package com.qa.consumer;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qa.consumer.persistence.repository.AllUsersRepository;
import com.qa.consumer.service.AllUserService;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.UserProducer;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.Trainer;
import com.qa.persistence.domain.TrainingManager;
import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.UserRequest.requestType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AllUserConsumerApplicationTests {

	@InjectMocks
	private AllUserService service;

	@Mock
	private AllUsersRepository repo;

	@Mock
	private UserProducer<User> producer;

	private UserRequest goodRequest;
	private UserRequest badRequest;

	private Trainee rob;
	private Trainer matt;
	private TrainingManager john;

	private ArrayList<User> allUsers;

	@Before
	public void setUp() {
		goodRequest = new UserRequest();
		badRequest = new UserRequest();

		rob = new Trainee();
		matt = new Trainer();
		john = new TrainingManager();

		allUsers = new ArrayList<User>();

		allUsers.add(rob);
		allUsers.add(john);
		allUsers.add(matt);

	}

	@Test
	public void testFindAllParse() {
		Mockito.when(repo.findAll()).thenReturn(allUsers);
		Mockito.when(producer.produce(allUsers, Constants.OUTGOING_ALLUSER_QUEUE_NAME))
				.thenReturn(Constants.USERS_QUEUED_MESSAGE);

		goodRequest.setHowToAct(requestType.READALL);

		assertEquals(Constants.USERS_QUEUED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.parse(badRequest));

	}

	@Test
	public void testDeleteAllParse() {
		goodRequest.setHowToAct(requestType.DELETEALL);

		assertEquals(Constants.USER_ALL_DELETED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.parse(badRequest));

	}

}
