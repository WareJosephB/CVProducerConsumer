package com.qa.consumer.persistence.repository;

import org.springframework.transaction.annotation.Transactional;

import com.qa.persistence.domain.TrainingManager;

@Transactional
public interface TrainingManagerRepository extends UserRepository<TrainingManager>{

}
