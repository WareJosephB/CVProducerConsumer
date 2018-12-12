package com.qa.consumer.service;

import java.util.Optional;

import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;

public interface UserServicable<T extends User> {

	public String parse(UserRequest request);

	public String add(UserRequest request);

	public String update(UserRequest userRequest);

	public String delete(UserRequest request);

	public String get(UserRequest request);

	public Iterable<User> getAll();

	public String promote(UserRequest request);

	public String deleteAll();

	public String send(String userName);

	public Optional<User> get(String userName);

	public User add(T user);

	public void delete(String userName);

	public void update(T userToUpdate, T updatedUser);

}
