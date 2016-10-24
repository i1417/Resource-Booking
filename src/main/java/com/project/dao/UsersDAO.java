/**
 * To perform database interaction for only users
 * @author Arpit Pittie
 */
package com.project.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import com.project.model.TokensModel;
import com.project.model.UsersModel;

@Repository("usersDAO")
@Transactional
public class UsersDAO {

	// To create a session for the database operation
	@Autowired
	private SessionFactory sessionFactory;

	// To get the beans
	@Autowired
	private ApplicationContext context;

	/**
	 * To perform the authentication of the user
	 * 
	 * @param userCredentials - The user credentials provided
 	 * @return - UsersModel containing information of the user
 	 * @author Arpit Pittie
	 */
	public UsersModel validateUserCustomLogin(UsersModel userCredentials) {
		// Creating a new session
		Session session = sessionFactory.openSession();

		// Creating a criteria query
		Criteria authentication = session.createCriteria(UsersModel.class);

		// Adding restrictions for login validation
		if (userCredentials.getPassword() == null) {
			authentication.add(Restrictions.and(Restrictions.eq("email",
					userCredentials.getEmail()), Restrictions.isNull("password")));
		} else {
			authentication
					.add(Restrictions.and(Restrictions.eq("email",
							userCredentials.getEmail()), Restrictions.eq(
							"password", userCredentials.getPassword())));
		}

		// Getting the result
		return (UsersModel) authentication.uniqueResult();
	}

	/**
	 * To create a new user account
	 * 
	 * @param userDetails - The UsersModel Object containing the details for the user
	 * @return - True if account is created successfully else false
	 * @author Arpit Pittie
	 */
	public boolean createUserAccount(UsersModel userDetails) {
		// Creating a new session
		Session session = sessionFactory.openSession();

		try {
			// Starting a new transaction
			session.beginTransaction();

			// Inserting the Users Details in the database
			session.save(userDetails);

			// Committing the current transaction
			session.getTransaction().commit();

			return true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			return false;
		}
	}

	/**
	 * To get the user details based on his/her email id
 	 * @param userDetails - The user model having the email id
	 * @return - The UsersModel object having the user details
	 * @author Arpit Pittie
	 */
	public UsersModel getUserDetailsByEmail(UsersModel userDetails) {
		// Creating a new session
		Session session = sessionFactory.openSession();

		// Creating a criteria query
		Criteria getDetails = session.createCriteria(UsersModel.class);

		// Adding restrictions for login validation
		getDetails.add(Restrictions.eq("email", userDetails.getEmail()));

		// Getting the result
		return (UsersModel) getDetails.uniqueResult();
	}

	/**
	 * Following function updates the existing user details 
	 * @param userDetailsModel(UsersModel) contains the Updated user details.
	 * @return true/false whether user details have been updated successfully or not.
	 * @author Arpit Pittie
	 */
	public boolean updateUserDetails(UsersModel userDetailsModel) {
		// Creating a new session
		Session session = sessionFactory.openSession();

		try {
			//Starting transaction
			session.beginTransaction();

			UsersModel objectToUpdate = (UsersModel) session.get(
					UsersModel.class, userDetailsModel.getEmployeeId());

			objectToUpdate.setName(userDetailsModel.getName());
			objectToUpdate.setDesignation(userDetailsModel.getDesignation());
			objectToUpdate.setMobileNumber(userDetailsModel.getMobileNumber());

			//Committing transaction
			session.getTransaction().commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			session.getTransaction().rollback();
			return false;
		}
	}

	/**
	 * Following function fetches the list of all the users.
	 * @return the list of all the users.
	 * @author Arpit Pittie
	 */
	@SuppressWarnings("unchecked")
	public List<UsersModel> getAllUsers() {
		// Creating a new session
		Session session = sessionFactory.openSession();

		Criteria cr = session.createCriteria(UsersModel.class);
		
		//getting the result containing distinct users.
		cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return cr.list();

	}
	
	/**
	 * To generate a link with unique token for the user to change his password
	 * @param userModel - The user details who wants to change his password
	 * @return - The unique token generated
	 * @author Arpit Pittie
	 */
	public long forgotPassword(UsersModel userModel) {
		Session session = sessionFactory.openSession();
		
		try {
			session.beginTransaction();
			TokensModel forgotToken = context.getBean(TokensModel.class);
			
			Criteria criteria = session.createCriteria(UsersModel.class);
			criteria.add(Restrictions.eq("email", userModel.getEmail()));
			
			//Getting the user details
			UsersModel customOrSocial = (UsersModel) criteria.uniqueResult();
			
			//Checking if the user has a account or not
			if(customOrSocial == null) {
				session.getTransaction().commit();
				return 0;
			} else if(customOrSocial.getPassword() == null) {
				//Checking if the user has used social login t create account
				session.getTransaction().commit();
				return -1;
			}
			
			//Inserting the user details
			forgotToken.setUser(customOrSocial);
			
			//Saving the token for the user
			session.save(forgotToken);
			
			//Getting the unique token generated
			long tokenId = forgotToken.getTokenId();
			
			session.getTransaction().commit();
			return tokenId;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return 0;
		}
	}
	
	/**
	 * To update the user password
	 * @param userModel - The user details with updated password
	 * @param token - The unique token used to change password
	 * @return - The Message regarding the change password status
	 * @author Arpit Pittie
	 */
	public String changePassword(UsersModel userModel, long token) {
		//Getting the session
		Session session = sessionFactory.openSession();
		
		try {
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(TokensModel.class);
			//Restrictions if the request exist and is not old than 4 hours
			criteria.add(Restrictions.and(Restrictions.eq("tokenId", token), Restrictions.sqlRestriction("TIMESTAMPDIFF(HOUR, {alias}.request, CURRENT_TIMESTAMP) < 4")));
			TokensModel tokenModel = (TokensModel) criteria.uniqueResult();
			
			//Token was not found checking i the link was expired
			if(tokenModel == null) {
				criteria = session.createCriteria(TokensModel.class);
				//Restriction to check if the request was made for that user or not
				criteria.add(Restrictions.eq("tokenId", token));
				tokenModel = (TokensModel) criteria.uniqueResult();
				
				//The link has expired to reset the password
				if(tokenModel != null) {
					session.delete(tokenModel);
				}
				session.getTransaction().commit();
				return "Link Expired";
			} else {
				//Updating the password for the user
				criteria = session.createCriteria(UsersModel.class);
				criteria.add(Restrictions.eq("email", userModel.getEmail()));
				UsersModel objToUpdate = (UsersModel) criteria.uniqueResult();
				
				objToUpdate.setPassword(userModel.getPassword());
				
				//Removing the token so that user can't revisit the same link
				session.delete(tokenModel);
				
				//Committing the transaction
				session.getTransaction().commit();
				
				return "Password Updated";
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return "Can't change the password";
		}
	}
}
