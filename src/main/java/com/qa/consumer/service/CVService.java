package com.qa.consumer.service;

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
		return RequestChecker.multiCVError(request);
	}

	private Iterable<CV> allCVs(UserRequest request) {
		if (RequestChecker.isValid(request) && RequestChecker.userExists(request, traineeRepo)) {
			return allCVs(request.getUsername());
		}
		return RequestChecker.multiCVError(request);
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
		return RequestChecker.multiError(request);
	}

	private Iterable<CV> getAll() {
		return cvRepo.findAll();
	}

	public Iterable<CV> search(CVRequest request) {
		if (RequestChecker.validSearch(request)) {
			return cvRepo.searchText(request.getSearchString());
		}
		return RequestChecker.multiError(request);
	}

	public Optional<CV> singleParse(CVRequest request) {
		if (request.getType() == requestType.READ) {
			return get(request.getcvIDtoActUpon());
		}
		return RequestChecker.singleError(request);
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
		if (RequestChecker.isValid(request)) {
			add(request.getCv());
			return Constants.CV_ADDED_MESSAGE;
		}
		return RequestChecker.errorMessage(request);
	}

	private CV add(CV cv) {
		return cvRepo.save(cv);
	}

	private String delete(CVRequest request) {
		if (RequestChecker.cvExists(request, cvRepo)) {
			delete(request.getcvIDtoActUpon());
			return Constants.CV_DELETED_MESSAGE;
		}
		return RequestChecker.errorMessageDelete(request, cvRepo);
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

}