package com.qa.consumer.persistence.repository;

import org.springframework.transaction.annotation.Transactional;

import com.qa.consumer.persistence.domain.User;

@Transactional
public interface AllUsersRepository extends UserRepository<User> {

}
