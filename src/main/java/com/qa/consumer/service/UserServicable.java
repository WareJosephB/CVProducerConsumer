package com.qa.consumer.service;

import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;

public interface UserServicable<T extends User> {

	public String messageParse(UserRequest request);

	public String add(UserRequest request);

	public String update(UserRequest userRequest);

	public void update(String username, User updatedUser);

	public String delete(UserRequest request);

	public void delete(String userName);

}
