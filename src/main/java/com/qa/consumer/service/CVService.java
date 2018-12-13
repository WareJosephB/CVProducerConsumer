package com.qa.consumer.service;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.consumer.persistence.repository.CVRepository;
import com.qa.consumer.persistence.repository.TraineeRepository;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.RequestChecker;
import com.qa.persistence.domain.CV;
import com.qa.persistence.domain.CVRequest;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.UserRequest;
import com.qa.persistence.domain.CVRequest.requestType;

@Service
public class CVService {

	@Autowired
	private CVRepository cvRepo;

	@Autowired
	private TraineeRepository traineeRepo;

	public Iterable<CV> multiParse(UserRequest request) {
		if (request.getHowToAct() == UserRequest.requestType.READALL) {
			return allCVs(request);
		}
		return multiError();
	}

	private Iterable<CV> allCVs(UserRequest request) {
		if (RequestChecker.isInvalid(request)) {
			return multiError();
		}
		if (RequestChecker.userExists(request, traineeRepo)) {
			return allCVs(request.getUsername());
		}
		return multiError();
	}

	private Iterable<CV> allCVs(String username) {
		Trainee cvWriter = (Trainee) traineeRepo.findById(username).get();
		return cvWriter.getCvList();

	}

	public Iterable<CV> multiParse(CVRequest request) {
		requestType type = request.getType();
		if (type == requestType.READALL) {
			return getAll();
		}
		if (type == requestType.SEARCH) {
			return search(request);
		}
		return multiError();
	}

	private Iterable<CV> getAll() {
		return cvRepo.findAll();
	}

	public Iterable<CV> search(CVRequest request) {
		if (RequestChecker.validSearch(request)) {
			return cvRepo.searchText(request.getSearchString());
		}
		return multiError();
	}

	public Optional<CV> singleParse(CVRequest request) {
		if (request.getType() == requestType.READ) {
			return get(request.getcvIDtoActUpon());
		}
		return singleError();
	}

	private Optional<CV> get(Long id) {
		return cvRepo.findById(id);
	}

	public String messageParse(CVRequest request) {
		requestType type = request.getType();
		switch (type) {
		case CREATE:
			return add(request);
		case DELETE:
			return delete(request);
		case UPDATE:
			return update(request);
		default:
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}
	}

	private String add(CVRequest request) {
		if (RequestChecker.isInvalid(request)) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		}
		add(request.getCv());
		return Constants.CV_ADDED_MESSAGE;
	}

	private CV add(CV cv) {
		return cvRepo.save(cv);
	}

	private String delete(CVRequest request) {
		Optional<CV> cvToDelete = get(request.getcvIDtoActUpon());
		if (!cvToDelete.isPresent()) {
			return Constants.CV_NOT_FOUND_MESSAGE;
		}
		delete(request.getcvIDtoActUpon());
		return Constants.CV_DELETED_MESSAGE;
	}

	private void delete(Long id) {
		cvRepo.deleteById(id);
	}

	private String update(CVRequest request) {
		if (RequestChecker.cvExists(request, cvRepo)) {
			update(request.getcvIDtoActUpon(), request.getCv());
			return Constants.CV_UPDATED_MESSAGE;
		}
		return Constants.CV_NOT_FOUND_MESSAGE;
	}

	private void update(Long cvIDToUpdate, CV updatedCV) {
		CV cvToUpdate = get(cvIDToUpdate).get();
		cvToUpdate.setCV(updatedCV.getCV());
	}

	public Iterable<CV> multiError() {
		ArrayList<CV> errorList = new ArrayList<>();
		CV errorMessage = new CV();
		errorMessage.setErrorMessage(Constants.MALFORMED_REQUEST_MESSAGE);
		errorList.add(errorMessage);
		return errorList;
	}

	public Optional<CV> singleError() {
		CV errorMessage = new CV();
		errorMessage.setErrorMessage(Constants.MALFORMED_REQUEST_MESSAGE);
		return Optional.of(errorMessage);
	}

}