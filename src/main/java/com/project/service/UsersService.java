/**
 * Class To implement the service layer for the Users
 * @author Arpit Pittie
 */
package com.project.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.project.dao.UsersDAO;
import com.project.model.BookingsModel;
import com.project.model.UsersModel;
import com.project.model.UsersVO;

@Service("usersService")
@Transactional
public class UsersService {

	// To interact with the DAO Layer
	@Autowired
	private UsersDAO usersDAO;

	// To get the beans
	@Autowired
	private ApplicationContext context;

	/**
	 * To validate the custom login credentials
	 * 
	 * @param userVO
	 *            - The User VO containing the credentials for login
	 * @return - True if the credentials are correct else false
	 * @author Arpit Pittie
	 */
	public UsersVO validateUserCustomLogin(UsersVO userVO) {
		// Getting the User Model object
		UsersModel userCredentials = context.getBean(UsersModel.class);

		// Copying the properties from VO to Model Object
		BeanUtils.copyProperties(userVO, userCredentials);

		try {
			// Getting the result from the database
			userCredentials = usersDAO.validateUserCustomLogin(userCredentials);

			// Checking if the user with the given credentials exist or not
			if (userCredentials == null) {
				return null;
			} else {
				List<BookingsModel> bookingList = userCredentials
						.getBookingsMade();

				// Removing all the pending bookings for the user
				for (int i = 0; i < bookingList.size();) {
					BookingsModel bookingsModel = bookingList.get(i);
					if (!bookingsModel.getStatus().equalsIgnoreCase("approved")) {
						bookingList.remove(i);
					} else {
						i++;
					}
				}

				// Copying the properties from Model to VO Object
				BeanUtils.copyProperties(userCredentials, userVO);

				userVO.setPassword(null);
				return userVO;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * To create a new account for the user
	 * 
	 * @param userDetails
	 *            - The UserVO containing the new account details
	 * @return - True if the account is created successfully else false
	 * @author Arpit Pittie
	 */
	public boolean createUserAccount(UsersVO userDetails) {
		// Getting the Users Model Object
		UsersModel userDetailsModel = context.getBean(UsersModel.class);

		// Copying the properties from VO to Model Object
		BeanUtils.copyProperties(userDetails, userDetailsModel);

		return usersDAO.createUserAccount(userDetailsModel);
	}

	/**
	 * To get the user details by its email id
	 * 
	 * @param userDetails
	 *            - The UsersVO containing the email id
	 * @return - UsersVO containing all the details for the user
	 * @author Arpit Pittie
	 */
	public UsersVO getUserDetailsByEmail(UsersVO userDetails) {
		// Getting the Users Model Object
		UsersModel userDetailsModel = context.getBean(UsersModel.class);

		// Copying the properties from VO to Model Object
		BeanUtils.copyProperties(userDetails, userDetailsModel);

		userDetailsModel = usersDAO.getUserDetailsByEmail(userDetailsModel);

		// Copying the properties from Model to VO Object
		BeanUtils.copyProperties(userDetailsModel, userDetails);

		return userDetails;
	}

	/**
	 * To check if the user exist or not
	 * 
	 * @param userDetails
	 *            - The user details to check if his account is already present
	 *            or not
	 * @return - True if user account exist else false
	 * @author Arpit Pittie
	 */
	public boolean checkUserExist(UsersVO userDetails) {
		// Getting the Users Model Object
		UsersModel userDetailsModel = context.getBean(UsersModel.class);

		// Copying the properties from VO to Model Object
		BeanUtils.copyProperties(userDetails, userDetailsModel);

		userDetailsModel = usersDAO.getUserDetailsByEmail(userDetailsModel);

		if (userDetailsModel == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * To update the user details
	 * 
	 * @param userDetails
	 *            - The updated user details
	 * @return - True if user details successfully updated else false
	 * @author Arpit Pittie
	 */
	public boolean updateUserDetails(UsersVO userDetails) {
		// Getting the Users Model Object
		UsersModel userDetailsModel = context.getBean(UsersModel.class);

		// Copying the properties from VO to Model Object
		BeanUtils.copyProperties(userDetails, userDetailsModel);

		return usersDAO.updateUserDetails(userDetailsModel);
	}

	/**
	 * To get the list of all users
	 * 
	 * @return - The list of users
	 * @author Vivek Mittal, Pratap Singh
	 */
	public List<UsersVO> getAllUsers() {
		List<UsersModel> usersModel = new ArrayList<UsersModel>();

		// Getting user list from dao
		usersModel = usersDAO.getAllUsers();
		List<UsersVO> usersVO = new ArrayList<UsersVO>(usersModel.size());
		UsersVO userVO;

		// Converting the userModel list to userVO list
		for (int i = 0; i < usersModel.size(); i++) {
			userVO = context.getBean(UsersVO.class);
			BeanUtils.copyProperties(usersModel.get(i), userVO);
			usersVO.add(userVO);
		}

		return usersVO;
	}

	/**
	 * To generate the forgot password link
	 * 
	 * @param user
	 *            - The user details who wants to reset the password
	 * @return - The unique token number for the forgot password request
	 * @author Arpit Pittie
	 */
	public long forgotPassword(UsersVO user) {
		UsersModel userModel = context.getBean(UsersModel.class);

		// Copying properties of userVO to userModel
		BeanUtils.copyProperties(user, userModel);

		// Getting the token id from dao
		return usersDAO.forgotPassword(userModel);
	}

	/**
	 * To change the password for the user
	 * 
	 * @param user
	 *            - The user details with the updated password
	 * @param token
	 *            - The token id for the forgot password
	 * @return - The status of change password for the user
	 * @author Arpit Pittie
	 */
	public String changePassword(UsersVO user, String token) {
		UsersModel userModel = context.getBean(UsersModel.class);

		// Copying properties of userVO to userModel
		BeanUtils.copyProperties(user, userModel);

		// Getting the response from the dao on password change
		return usersDAO.changePassword(userModel, Long.parseLong(token));
	}
}
