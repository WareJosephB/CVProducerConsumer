package com.qa.consumer.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.consumer.persistence.domain.CV;
import com.qa.consumer.persistence.domain.CVRequest;
import com.qa.consumer.persistence.domain.CVRequest.requestType;
import com.qa.consumer.persistence.repository.CVRepository;
import com.qa.consumer.util.CVProducer;
import com.qa.consumer.util.Constants;

@Service
public class CVService {

	@Autowired
	private CVRepository consumerRepo;

	@Autowired
	private CVProducer producer;

	public void setRepo(CVRepository persist) {
		this.consumerRepo = persist;
	}

	private Iterable<CV> getAll() {
		return consumerRepo.findAll();
	}

	private Optional<CV> get(Long id) {
		return consumerRepo.findById(id);
	}

	private CV add(CV cv) {
		return consumerRepo.save(cv);
	}

	private void delete(Long id) {
		consumerRepo.deleteById(id);
	}

	private void update(CV cvToUpdate, CV updatedCV) {
		cvToUpdate.setCV(updatedCV.getCV());
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

	public String parse(CVRequest request) {
		if (request.getType() == requestType.CREATE) {
			return add(request);
		} else if (request.getType() == requestType.DELETE) {
			return delete(request);
		} else if (request.getType() == requestType.READ) {
			return send(get(request.getcvIDtoActUpon()));
		} else if (request.getType() == requestType.UPDATE) {
			return update(request);
		} else if (request.getType() == requestType.READALL) {
			return send(getAll());
		} else if (request.getType() == requestType.SEARCH) {
			return search(request);
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

	private String send(Iterable<CV> all) {
		return producer.produce(all);

	}

	public String send(Optional<CV> optional) {
		if (!optional.isPresent()) {
			return Constants.CV_NOT_FOUND_MESSAGE;
		} else {
			return producer.produce(optional.get());

		}
	}

	public String search(CVRequest request) {
		if (request.getSearchString() == null) {
			return Constants.MALFORMED_REQUEST_MESSAGE;
		} else {
			return send(consumerRepo.searchText(request.getSearchString()));
		}
	}

}