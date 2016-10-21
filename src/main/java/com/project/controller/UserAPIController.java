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
import com.project.service.MailService;
import com.project.service.UsersService;

@Controller
public class UserAPIController {

	// To interact with the service layer
	@Autowired
	private UsersService usersService;

	@Autowired
	private MailService mailService;

	// To get the beans
	@Autowired
	private ApplicationContext context;

	/**
	 * To validate the login request from the user
	 * @param userCredentials(UsersVO) - The UsersVO object containing the user's login credentials
	 * @return - Response object stating whether the credentials are right or wrong
	 * @author- Arpit Pittie
	 */
	@RequestMapping(value = "/validate/custom", method = RequestMethod.POST)
	public @ResponseBody Response customLoginStatus(
			@RequestBody UsersVO userCredentials) {
		// Getting the result from Service Layer
		UsersVO result = usersService.validateUserCustomLogin(userCredentials);

		// Sending back the response to the client
		if (result != null) {
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
	 * @author- Arpit Pittie
	 */
	@RequestMapping(value = "createAccount", method = RequestMethod.POST)
	public @ResponseBody Response createUserAccount(
			@RequestBody UsersVO userDetails) {
		/*
		 * Enumeration en = request.getParameterNames();
		 * while(en.hasMoreElements()) { Object objOri=en.nextElement(); String
		 * param=(String)objOri; String value=request.getParameter(param);
		 * System
		 * .out.println("Parameter Name is '"+param+"' and Parameter Value is '"
		 * +value+"'"); }
		 */

		// Sending the data to the Service Layer for creation of the account
		boolean result = usersService.createUserAccount(userDetails);

		// Sending back the response to the client
		if (result) {
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
	 * @author- Arpit Pittie
	 */
	@RequestMapping("userDetailsByEmail")
	public @ResponseBody Response getUserDetailsByEmail(
			@RequestBody UsersVO userDetails) {
		// Getting the user details from Service Layer
		userDetails = usersService.getUserDetailsByEmail(userDetails);

		// Checking if the user exists or not
		if (userDetails == null) {
			return new Response(403, "User does not exist");
		} else {
			return new Response(200, userDetails);
		}
	}

	/**
	 * To check if user exist or not.
	 * @param userDetails - The UsersVO object containing the email to check if user exist or not
	 * @return - Response object confirming user exist or not
	 * @author- Arpit Pittie
	 */
	@RequestMapping("userExist")
	public @ResponseBody Response userExist(@RequestBody UsersVO userDetails) {
		// Getting the result from Service layer
		boolean result = usersService.checkUserExist(userDetails);
		// Checking if the user exists or not
		if (result) {
			return new Response(200, "User Exist");
		} else {
			return new Response(403, "User does not exist");
		}
	}

	/**
	 * To update the user details
	 * @param userDetails(UsersVO) - The updated User Details
	 * @return - Response object confirming the updation
	 * @author- Pratap Singh , Vivek Mittal
	 */
	@RequestMapping("user/update")
	public @ResponseBody Response updateUserDetails(
			@RequestBody UsersVO userDetails) {
		// Getting the result from Service layer
		boolean result = usersService.updateUserDetails(userDetails);

		// Checking if the user exists or not
		if (result) {
			// mailService.sendMail(userDetails);
			return new Response(200, userDetails);
		} else {
			return new Response(403, "User details could not be updated");
		}
	}

	/**
	 * To fetch the list of all users
	 * @return the response object containing the list of all users
	 * @author- Pratap Singh , Vivek Mittal
	 */
	@RequestMapping(value = "/users/getAll", method = RequestMethod.GET)
	public @ResponseBody Response getAllUsers() {
		// Getting the result from Service layer
		List<UsersVO> result = usersService.getAllUsers();

		// Sending back the response to the client
		if (result != null) {
			System.out.println("OK");
			return new Response(200, result);
		} else {
			System.out.println("Wrong");
			return new Response(400, "Couldn't fetch all the Users");
		}
	}
}
