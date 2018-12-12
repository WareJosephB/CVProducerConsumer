package com.qa.consumer.persistence.repository;

import org.springframework.transaction.annotation.Transactional;

import com.qa.persistence.domain.Trainer;

@Transactional
public interface TrainerRepository extends UserRepository<Trainer> {

}
