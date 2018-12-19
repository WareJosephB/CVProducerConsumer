package com.qa.consumer.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qa.persistence.domain.TrainingManager;

public interface TrainingManagerRepository extends MongoRepository<TrainingManager, String> {

}
