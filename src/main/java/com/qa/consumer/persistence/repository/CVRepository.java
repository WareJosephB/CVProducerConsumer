package com.qa.consumer.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.qa.persistence.domain.CV;

@Transactional
public interface CVRepository extends MongoRepository<CV, Long> {
	
	@Query("{ 'text' : ?0 }")
	Iterable<CV> searchText(String searchString);

}