package com.qa.consumer.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.qa.persistence.domain.User;

@NoRepositoryBean
public abstract interface UserRepository<T extends User> extends MongoRepository<User, String> {

}
