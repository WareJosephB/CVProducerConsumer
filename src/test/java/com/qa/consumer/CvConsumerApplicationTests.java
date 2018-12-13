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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.qa.consumer.persistence.repository.CVRepository;
import com.qa.consumer.service.CVService;

import com.qa.consumer.util.Constants;
import com.qa.persistence.domain.CV;
import com.qa.persistence.domain.CVRequest;
import com.qa.persistence.domain.CVRequest.requestType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CvConsumerApplicationTests {

	@Autowired
	private JmsTemplate jmsTemplate;

	@InjectMocks
	private CVService service;

	@Mock
	private CVRepository repo;



	private CVRequest goodRequest;
	private CVRequest badRequest;
	private CV cv;
	private CV cv2;
	private ArrayList<CV> allCVs;

	@Before
	public void setUp() {
		goodRequest = new CVRequest();
		goodRequest.setcvIDtoActUpon(1l);
		badRequest = new CVRequest();
		badRequest.setcvIDtoActUpon(11l);

		cv = new CV();
		cv2 = new CV();
		allCVs = new ArrayList<CV>();
		allCVs.add(cv);
		allCVs.add(cv2);

	}

	@Test
	public void testSendReceive() { // Checks whether correctly pointing to MQ Server and therefore requires one
		jmsTemplate.convertAndSend("testQueue", "Hello World!");
		assertEquals("Hello World!", jmsTemplate.receiveAndConvert("testQueue"));
	}

	@Test
	public void testFindParse() {
		Mockito.when(repo.findById(1l)).thenReturn(Optional.of(cv));
		Mockito.when(repo.findById(11l)).thenReturn(Optional.empty());
		
		goodRequest.setType(requestType.READ);
		badRequest.setType(requestType.READ);

		assertEquals(Optional.of(cv), service.singleParse(goodRequest));
		assertEquals(Optional.empty(), service.singleParse(badRequest));

	}

	@Test
	public void testDeleteParse() {
		Mockito.when(repo.findById(1l)).thenReturn(Optional.of(cv));
		Mockito.when(repo.findById(11l)).thenReturn(Optional.empty());

		goodRequest.setType(requestType.DELETE);
		badRequest.setType(requestType.DELETE);

		assertEquals(Constants.CV_DELETED_MESSAGE, service.messageParse(goodRequest));
		assertEquals(Constants.CV_NOT_FOUND_MESSAGE, service.messageParse(badRequest));
	}

	@Test
	public void testUpdateParse() {
		Mockito.when(repo.findById(1l)).thenReturn(Optional.of(cv));
		Mockito.when(repo.findById(11l)).thenReturn(Optional.empty());

		goodRequest.setType(requestType.UPDATE);
		goodRequest.setCv(cv2);
		cv2.setCvid(1);
		badRequest.setType(requestType.UPDATE);

		assertEquals(Constants.CV_UPDATED_MESSAGE, service.messageParse(goodRequest));
		assertEquals(Constants.CV_NOT_FOUND_MESSAGE, service.messageParse(badRequest));
	}

	@Test
	public void testAddParse() {
		goodRequest.setType(requestType.CREATE);
		goodRequest.setCv(cv2);
		badRequest.setType(requestType.CREATE);

		assertEquals(Constants.CV_ADDED_MESSAGE, service.messageParse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.messageParse(badRequest));

	}

	@Test
	public void testFindAllAndMalformedParse() {
		Mockito.when(repo.findAll()).thenReturn(allCVs);
		
		goodRequest.setType(requestType.READALL);

		assertEquals(allCVs, service.multiParse(goodRequest));
		assertEquals(service.multiError().toString(), service.multiParse(badRequest).toString());
	}

	@Test
	public void testSearchFunction() {
		Mockito.when(repo.searchText("Test String")).thenReturn(allCVs);
		
		goodRequest.setType(requestType.SEARCH);
		goodRequest.setSearchString("Test String");
		badRequest.setType(requestType.SEARCH);

		assertEquals(allCVs, service.multiParse(goodRequest));
		assertEquals(service.multiError().toString(), service.multiParse(badRequest).toString());
	}

}
