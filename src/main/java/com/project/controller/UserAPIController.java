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
import org.springframework.web.bind.annotation.RequestParam;
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
		// Getting the result from the Service Layer
		UsersVO result = usersService.validateUserCustomLogin(userCredentials);

		// Sending back the response to the client
		if (result != null) {
			return new Response(200, result);
		} else {
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

		// Sending the data to the Service Layer for creation of the account
		boolean result = usersService.createUserAccount(userDetails);

		// Sending back the response to the client
		if (result) {
			String mailMessage = "<p>Dear "+ userDetails.getName() +"</p><p>Your account has been created</p><p>Please follow the link to access your account</p>" 
					+ "<div style='display: inline-block; background-color:#5cb85c; padding: 8px 15px; border-radius:5px; margin:8px 15px; border:1px solid white'>"
								+ "<a style='color: white; text-decoration: none;' href='http://localhost:8080/Project-Authentication/' >Click Here</a>"
								+ "</div>"
						+ "</p><br/><p>Regards</p><p>Resource Booking Team</p>";
				mailService.sendHTMLMail(userDetails,
						"Account created on Resource Booking", mailMessage);
				
			return new Response(200, "OK");
		} else {
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
		// Getting the user details
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
		// Checking if the user exists or not
		if (usersService.checkUserExist(userDetails)) {
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
	@RequestMapping(value = "/user/getAll", method = RequestMethod.GET)
	public @ResponseBody Response getAllUsers() {
		// Getting the result from the Service Layer
		List<UsersVO> result = usersService.getAllUsers();

		// Sending back the response to the client
		if (result != null) {
			return new Response(200, result);
		} else {
			return new Response(400, "Couldn't fetch all the Users");
		}
	}
	
	/**
	 * To generate the link to change password for a user
	 * @param user - The user details who wants to generate a change password link
	 * @return - Response object confirming the link generation
	 * @author Arpit Pittie
	 */
	@RequestMapping(value = "/forgotPass", method = RequestMethod.POST)
	public @ResponseBody Response forgotPassword(@RequestBody UsersVO user) {
		long token = usersService.forgotPassword(user);
		
		//Checking if the token is generated or not
		if(token > 0) {
			//Sending the forgot password emil to the user
			String mailMessage = "<p>Dear User</p><p>Please follow the below link to change your password" 
				+ "<div style='display: inline-block; background-color:#5cb85c; padding: 8px 15px; border-radius:5px; margin:8px 15px; border:1px solid white'>"
							+ "<a style='color: white; text-decoration: none;' href='http://localhost:8080/Project-Authentication/forgotPassword.html?token="
							+ token + "&email="
							+ user.getEmail() + "' >Change Password</a>"
							+ "</div>"
					+ "</p><br/><p>Regards</p><p>Resource Booking Team</p>";
			mailService.sendHTMLMail(user,
					"Change Password for Account on Resource Booking", mailMessage);
					
			return new Response(200, "Link Generated");
		} else if(token < 0){
			return new Response(400, "You had used social login");
		} else {
			return new Response(400, "User does not Exist");
		}
	}
	
	/**
	 * To update the user password
	 * @param user - The user details whom the password is to be updated
	 * @param token - The unique token for the forgot password request
	 * @return - Response object confirming the password updation
	 * @author Arpit Pittie
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public @ResponseBody Response changePassword(@RequestBody UsersVO user, @RequestParam("token") String token) {
		return new Response(200, usersService.changePassword(user, token));
	}
	
	/**
	 * To send a mail invite to a person
	 * @param userToInvite - The user details to invite
	 * @return - Response object confirming the invite sent
	 * @author Arpit Pittie
	 */
	@RequestMapping(value = "/user/sendInvitationToUser", method = RequestMethod.POST)
	public @ResponseBody Response sendInvitationToUser(@RequestBody UsersVO userToInvite) {
		//Sending mail invite to a user for creating an account
		String mailMessage = "<p>Dear "+ userToInvite.getName() +"</p><p>A friend of your has invited you to join the Resource Booking.</p><p>Please follow the link to create the account</p>" 
				+ "<div style='display: inline-block; background-color:#5cb85c; padding: 8px 15px; border-radius:5px; margin:8px 15px; border:1px solid white'>"
							+ "<a style='color: white; text-decoration: none;' href='http://localhost:8080/Project-Authentication/' >Click Here</a>"
							+ "</div>"
					+ "</p><br/><p>Regards</p><p>Resource Booking Team</p>";
			mailService.sendHTMLMail(userToInvite,
					"Invitation to join Resource Booking", mailMessage);
			
		return new Response(200, "Invitation Sent");
	}
}
