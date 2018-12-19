package com.qa.consumer.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qa.persistence.domain.Trainee;

public interface TraineeRepository extends MongoRepository<Trainee, String> {

}
