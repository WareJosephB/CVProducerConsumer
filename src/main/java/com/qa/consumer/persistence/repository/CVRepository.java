package com.qa.consumer.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.qa.consumer.persistence.domain.CV;

public interface CVRepository extends MongoRepository<CV, Long> {
	
	@Query("{ 'text' : ?0 }")
	Iterable<CV> searchText(String searchString);

}