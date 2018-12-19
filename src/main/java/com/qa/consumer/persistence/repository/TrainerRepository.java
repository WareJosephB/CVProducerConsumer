package com.qa.consumer.persistence.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.qa.persistence.domain.Trainer;


public interface TrainerRepository extends MongoRepository<Trainer, String> {

}
