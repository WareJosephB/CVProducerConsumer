package com.qa.consumer.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qa.consumer.persistence.repository.CVRepository;
import com.qa.consumer.persistence.repository.TraineeRepository;
import com.qa.consumer.util.Constants;
import com.qa.consumer.util.EmailService;
import com.qa.consumer.util.RequestChecker;
import com.qa.persistence.domain.CV;
import com.qa.persistence.domain.CVRequest;
import com.qa.persistence.domain.CVRequest.requestType;

@Service
@Transactional
public class CVService {

	@Autowired
	private CVRepository cvRepo;

	@Autowired
	private TraineeRepository traineeRepo;

	@Autowired
	private EmailService email;

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
			if (RequestChecker.tagged(request, traineeRepo)) {
				email.email(request);
			}
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
			if (RequestChecker.tagged(request, traineeRepo)) {
				email.email(request);
			}
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
			if (RequestChecker.tagged(request, traineeRepo)) {
				email.email(request);
			}
			update(request.getcvIDtoActUpon(), request.getCv());
			return Constants.CV_UPDATED_MESSAGE;
		}
		return Constants.CV_NOT_FOUND_MESSAGE;
	}

	private void update(Long cvIDToUpdate, CV updatedCV) {
		CV cvToUpdate = get(cvIDToUpdate).get();
		cvToUpdate.setContents(updatedCV.getContents());
		cvRepo.save(cvToUpdate);
	}

}