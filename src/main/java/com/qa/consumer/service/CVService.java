package com.qa.consumer.service;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.consumer.persistence.repository.CVRepository;
import com.qa.consumer.persistence.repository.TraineeRepository;
import com.qa.consumer.util.Constants;
import com.qa.persistence.domain.CV;
import com.qa.persistence.domain.CVRequest;
import com.qa.persistence.domain.Trainee;
import com.qa.persistence.domain.User;
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
		if (request.getUserToAddOrUpdate() == null) {
			return multiError();
		} else {
			Optional<User> author = traineeRepo.findById(request.getUserToAddOrUpdate().getUsername());
			if (author.isPresent()) {
				Trainee cvWriter = (Trainee) author.get();
				return cvWriter.getCvList();
			} else {
				return multiError();
			}
		}
	}

	public Iterable<CV> multiParse(CVRequest request) {
		requestType type = request.getType();
		if (type == requestType.READALL) {
			return getAll();
		} else if(type == requestType.SEARCH)
			return search(request);
		return multiError();
	}

	private Iterable<CV> getAll() {
		return cvRepo.findAll();
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
		if (request.getType() == requestType.CREATE) {
			return add(request);
		} else if (request.getType() == requestType.DELETE) {
			return delete(request);
		} else if (request.getType() == requestType.UPDATE) {
			return update(request);
		}
		return Constants.MALFORMED_REQUEST_MESSAGE;
	}

	private String add(CVRequest request) {
		if (request.getCv() == null) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		} else {
			add(request.getCv());
			return Constants.CV_ADDED_MESSAGE;
		}
	}

	private CV add(CV cv) {
		return cvRepo.save(cv);
	}

	private String delete(CVRequest request) {
		Optional<CV> cvToDelete = get(request.getcvIDtoActUpon());
		if (!cvToDelete.isPresent()) {
			return Constants.CV_NOT_FOUND_MESSAGE;
		} else {
			delete(request.getcvIDtoActUpon());
			return Constants.CV_DELETED_MESSAGE;
		}
	}

	private void delete(Long id) {
		cvRepo.deleteById(id);
	}

	private String update(CVRequest request) {
		Optional<CV> cvToUpdate = get(request.getcvIDtoActUpon());
		CV updatedCV = request.getCv();
		if (!cvToUpdate.isPresent()) {
			return Constants.CV_NOT_FOUND_MESSAGE;
		} else {
			update(cvToUpdate.get(), updatedCV);
			return Constants.CV_UPDATED_MESSAGE;
		}
	}

	private void update(CV cvToUpdate, CV updatedCV) {
		cvToUpdate.setCV(updatedCV.getCV());
	}

	public Iterable<CV> search(CVRequest request) {
		if (request.getSearchString() == null) {
			return multiError();
		} else {
			return cvRepo.searchText(request.getSearchString());
		}
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