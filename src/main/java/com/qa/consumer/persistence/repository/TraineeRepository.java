package com.qa.consumer.persistence.repository;

import org.springframework.transaction.annotation.Transactional;

import com.qa.persistence.domain.Trainee;

@Transactional
public interface TraineeRepository extends UserRepository<Trainee> {

}
