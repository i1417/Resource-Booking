/**
 * Class to manage the api
 * @author Arpit Pittie
 */
package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.model.UsersVO;
import com.project.service.UsersFacade;

@Controller
public class UserAPIController {
	
	//To interact with the facade layer
	@Autowired
	private UsersFacade usersFacade;
	
	//To get the beans
	@Autowired
	private ApplicationContext context;

	/**
	 * To validate the login request from the user
	 * @param userCredentials - The UsersVO object containing the user's login credentials
	 * @return - Response object stating whether the credentials are right or wrong
	 */
	@RequestMapping(value = "/validate/custom", method = RequestMethod.POST)
	public @ResponseBody Response customLoginStatus(@RequestBody UsersVO userCredentials) {
		//Getting the result from the facade
		UsersVO result = usersFacade.validateUserCustomLogin(userCredentials);
		
		//Sending back the response to the client
		if(result != null) {
			System.out.println("OK");
			return new Response(200, result);
		} else {
			System.out.println("Wrong");
			return new Response(400, "Wrong Credentials");
		}
	}
	
	/**
	 * To save the user details to create a new account
	 * @param userDetails - The UsersVO object containing the user details for new account
	 * @return - Response object stating whether the account is created or not
	 */
	@RequestMapping(value = "createAccount", method = RequestMethod.POST)
	public @ResponseBody Response createUserAccount(@RequestBody UsersVO userDetails) {
		/*Enumeration en = request.getParameterNames();
		while(en.hasMoreElements())
		{
			Object objOri=en.nextElement();
			String param=(String)objOri;
			String value=request.getParameter(param);
			System.out.println("Parameter Name is '"+param+"' and Parameter Value is '"+value+"'");
		}		*/
		
		//Sending the data to the facade for creation of the account
		boolean result = usersFacade.createUserAccount(userDetails);
		//boolean result = true;
		
		//Sending back the response to the client
		if(result) {
			System.out.println("OK");
			return new Response(200, "OK");
		} else {
			System.out.println("Wrong");
			return new Response(400, "Account already present");
		}
	}
	
	/**
	 * To get the all the user profile details using its email id
	 * @param userDetails - The UsersVO object containing the email id to fetch the personal details
	 * @return - Response object having the user details
	 */
	@RequestMapping("userDetailsByEmail")
	public @ResponseBody Response getUserDetailsByEmail(@RequestBody UsersVO userDetails) {
		//Getting the user details
		userDetails = usersFacade.getUserDetailsByEmail(userDetails);
		
		//Checking if the user exists or not
		if(userDetails == null) {
			return new Response(403, "User does not exist");
		} else {
			return new Response(200, userDetails);
		}
	}
	
	/**
	 * To check if user exist or not
	 * @param userDetails - The UsersVO object containing the email to check if user exist or not
	 * @return - Response object confirming user exist or not
	 */
	@RequestMapping("userExist")
	public @ResponseBody Response userExist(@RequestBody UsersVO userDetails) {
		//Checking if the user exists or not
		if(usersFacade.checkUserExist(userDetails)) {
			return new Response(200, "User Exist");
		} else {
			return new Response(403, "User does not exist");
		}
	}
	
	/**
	 * To update the user details
	 * @param userDetails - The updated User Details
	 * @return - Response object confirming the updation
	 */
	@RequestMapping("user/update")
	public @ResponseBody Response updateUserDetails(@RequestBody UsersVO userDetails) {
		userDetails = usersFacade.updateUserDetails(userDetails);
		
		//Checking if the user exists or not
		if(userDetails != null) {
			return new Response(200, userDetails);
		} else {
			return new Response(403, "User details not correct");
		}
	}
	
	/**
	 * To fetch the list of all users
	 * @return the response object containing the list of all users
	 */
	@RequestMapping(value = "/users/getAll", method = RequestMethod.GET)
	public @ResponseBody Response getAllUsers() {
		//Getting the result from the facade
		List<UsersVO> result = usersFacade.getAllUsers();
		
		//Sending back the response to the client
		if(result != null) {
			System.out.println("OK");
			return new Response(200, result);
		} else {
			System.out.println("Wrong");
			return new Response(400, "Wrong Credentials");
		}
	}
	
}
