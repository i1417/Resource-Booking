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

	public boolean updateUserDetails(UsersVO userDetails) {
		// Getting the Users Model Object
		UsersModel userDetailsModel = context.getBean(UsersModel.class);

		// Copying the properties from VO to Model Object
		BeanUtils.copyProperties(userDetails, userDetailsModel);

		return usersDAO.updateUserDetails(userDetailsModel);
	}

	public List<UsersVO> getAllUsers() {
		List<UsersModel> usersModel = new ArrayList<UsersModel>();

		usersModel = usersDAO.getAllUsers();
		List<UsersVO> usersVO = new ArrayList<UsersVO>(usersModel.size());
		UsersVO userVO;
		System.out.println(" model data" + usersModel.size());
		for (int i = 0; i < usersModel.size(); i++) {
			userVO = context.getBean(UsersVO.class);
			BeanUtils.copyProperties(usersModel.get(i), userVO);
			usersVO.add(userVO);
		}

		System.out.println("vo data" + usersVO);
		return usersVO;
	}
	
	public long forgotPassword(UsersVO user) {
		UsersModel userModel = context.getBean(UsersModel.class);
		
		BeanUtils.copyProperties(user, userModel);
		
		return usersDAO.forgotPassword(userModel);
		
	}
	
	public String changePassword(UsersVO user, String token) {
		UsersModel userModel = context.getBean(UsersModel.class);
		
		BeanUtils.copyProperties(user, userModel);
		return usersDAO.changePassword(userModel, Long.parseLong(token));
	}
}
