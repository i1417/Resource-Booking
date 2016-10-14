/**
 * Class to provide a single API for the database interaction with the controllers
 * @author Arpit Pittie
 */
package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.project.model.UsersVO;

@Service("usersFacade")
public class UsersFacade {

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private ApplicationContext context;	//To get the beans
	
	/**
	 * To validate the custom login user credentials
	 * @param userVO - The User VO containing the credentials
	 * @return - True if the credentials are right else false
	 */
	public UsersVO validateUserCustomLogin(UsersVO userVO) {
		return usersService.validateUserCustomLogin(userVO);
	}
	
	/**
	 * To create a user account
	 * @param userDetails - The UserVO containing the details for a new account
	 * @return - True if account is created successfully else false
	 */
	public boolean createUserAccount(UsersVO userDetails) {
		return usersService.createUserAccount(userDetails);
	}
	
	public UsersVO getUserDetailsByEmail(UsersVO userDetails) {
		return usersService.getUserDetailsByEmail(userDetails);
	}
	
	public boolean checkUserExist(UsersVO userDetails) {
		return usersService.checkUserExist(userDetails);
	}
	
	public UsersVO updateUserDetails(UsersVO userDetails) {
		boolean result = usersService.updateUserDetails(userDetails);
		
		if(result) {
			return getUserDetailsByEmail(userDetails);
		} else {
			return null;
		} 
	}
}
