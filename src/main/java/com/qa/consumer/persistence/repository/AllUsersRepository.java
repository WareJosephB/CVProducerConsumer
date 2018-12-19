package com.qa.consumer.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qa.persistence.domain.User;

public interface AllUsersRepository extends MongoRepository<User, String> {

}
