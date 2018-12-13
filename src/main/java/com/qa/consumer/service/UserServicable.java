package com.qa.consumer.service;

import java.util.Optional;

import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;

public interface UserServicable<T extends User> {

	public String messageParse(UserRequest request);

	public String add(UserRequest request);

	public User add(T user);

	public String update(UserRequest userRequest);

	public void update(T userToUpdate, T updatedUser);

	public String delete(UserRequest request);

	public void delete(String userName);

	public String promote(UserRequest request);

	public Iterable<User> multiParse(UserRequest request);

	public Iterable<User> getAll();

	public Iterable<User> multiError();

	public Optional<User> singleParse(UserRequest request);

	public Optional<User> get(UserRequest request);

	public Optional<User> get(String userName);

	public Optional<User> singleError();

}
