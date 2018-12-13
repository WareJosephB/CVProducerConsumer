package com.qa.consumer.service;

import com.qa.persistence.domain.User;
import com.qa.persistence.domain.UserRequest;

public interface PromotableUserServicable<T extends User> extends UserServicable<User> {

	public String promote(UserRequest request);

	public void promote(String username);

}
