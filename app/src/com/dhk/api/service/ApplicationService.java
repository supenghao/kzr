package com.dhk.api.service;

import com.dhk.api.entity.User;

public interface ApplicationService {

	boolean insertUser(String name, String pwd);

	User findUserByName(String loginName);

	boolean cheakUserPsw(String userName, String pwd);

}
