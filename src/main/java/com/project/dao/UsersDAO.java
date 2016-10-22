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
	 * @param userCredentials
	 *            - The user credentials provided
	 * @return - UsersModel containing information for the user
	 * @throws Exception
	 */
	public UsersModel validateUserCustomLogin(UsersModel userCredentials)
			throws Exception {
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
	 * @param userDetails
	 *            - The UsersModel Object containing the details for the user
	 * @return - True if account is created successfully else false
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
	 * To get the user details based on his email id
	 * 
	 * @param userDetails
	 *            - The user model having the email id
	 * @return - The UsersModel object having the user details
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

	public boolean updateUserDetails(UsersModel userDetailsModel) {
		Session session = sessionFactory.openSession();

		try {
			session.beginTransaction();

			UsersModel objectToUpdate = (UsersModel) session.get(
					UsersModel.class, userDetailsModel.getEmployeeId());

			objectToUpdate.setName(userDetailsModel.getName());
			objectToUpdate.setDesignation(userDetailsModel.getDesignation());
			objectToUpdate.setMobileNumber(userDetailsModel.getMobileNumber());

			session.getTransaction().commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			session.getTransaction().rollback();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<UsersModel> getAllUsers() {
		// Creating a new session
		Session session = sessionFactory.openSession();

		Criteria cr = session.createCriteria(UsersModel.class);
		cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return cr.list();

	}
	
	public long forgotPassword(UsersModel userModel) {
		Session session = sessionFactory.openSession();
		
		try {
			session.beginTransaction();
			TokensModel forgotToken = context.getBean(TokensModel.class);
			
			Criteria criteria = session.createCriteria(UsersModel.class);
			criteria.add(Restrictions.eq("email", userModel.getEmail()));
			
			UsersModel customOrSocial = (UsersModel) criteria.uniqueResult();
			
			if(customOrSocial == null) {
				session.getTransaction().commit();
				return 0;
			} else if(customOrSocial.getPassword() == null) {
				session.getTransaction().commit();
				return 0;
			}
			
			forgotToken.setUser(customOrSocial);
			
			session.save(forgotToken);
			long tokenId = forgotToken.getTokenId();
			
			session.getTransaction().commit();
			return tokenId;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return 0;
		}
	}
	
	public String changePassword(UsersModel userModel, long token) {
		Session session = sessionFactory.openSession();
		
		try {
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(TokensModel.class);
			criteria.add(Restrictions.and(Restrictions.eq("tokenId", token), Restrictions.sqlRestriction("TIMESTAMPDIFF(HOUR, {alias}.request, CURRENT_TIMESTAMP) < 4")));
			TokensModel tokenModel = (TokensModel) criteria.uniqueResult();
			
			if(tokenModel == null) {
				criteria = session.createCriteria(TokensModel.class);
				criteria.add(Restrictions.eq("tokenId", token));
				tokenModel = (TokensModel) criteria.uniqueResult();
				
				if(tokenModel != null) {
					session.delete(tokenModel);
				}
				session.getTransaction().commit();
				return "Link Expired";
			} else {
				criteria = session.createCriteria(UsersModel.class);
				criteria.add(Restrictions.eq("email", userModel.getEmail()));
				//criteria.setProjection(Projections.property("employeeId"));
				UsersModel objToUpdate = (UsersModel) criteria.uniqueResult();
				
				objToUpdate.setPassword(userModel.getPassword());
				
				session.delete(tokenModel);
				
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
