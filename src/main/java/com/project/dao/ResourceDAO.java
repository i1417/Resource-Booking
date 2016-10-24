/**
 * To perform database interaction regarding resources
 * @author Pratap Singh Ranawat and Vivek Mittal
 */
package com.project.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.model.ResourcesModel;
import com.project.model.UsersModel;

@Repository("resourceDAO")
@Transactional
public class ResourceDAO {

	// To create a session for the database operation
	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Following function fires query to database to get the required result(i.e
	 * get all the available resources)
	 * @return the list of all available resources .
	 * @author Vivek Mittal, Pratap Singh
	 */
	@SuppressWarnings("unchecked")
	public List<ResourcesModel> allResourceList() {

		Session session = sessionFactory.openSession();

		// Getting the result
		return session.createCriteria(ResourcesModel.class).list();

	}

	/**
	 * Following function helps in creation of a new resource.
	 * 
	 * @param resourcesModel
	 *            contains the information of the new resource.
	 * @return true/false whether resource created successfully or not
	 * @author Arpit Pittie, Vivek Mittal, Pratap Singh
	 */
	public boolean createResource(ResourcesModel resourceModel) {
		// Creating a new session
		Session session = sessionFactory.openSession();

		try {
			// Starting a new transaction
			session.beginTransaction();

			// Inserting the Users Details in the database
			session.save(resourceModel);

			// Committing the current transaction
			session.getTransaction().commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}
	}

	/**
	 * Following function helps in updating the existing resource.
	 * 
	 * @param resourcesModel(ResourcesModel) contains the information of the resource to be edited.
 	 * @return true/false whether resource edited successfully or not
 	 * @author Arpit Pittie
	 */
	public boolean editResource(ResourcesModel resourceModel) {
		//getting session
		Session session = sessionFactory.openSession();

		try {
			//starting a transaction
			session.beginTransaction();

			ResourcesModel objectToUpdate = (ResourcesModel) session.get(
					ResourcesModel.class, resourceModel.getResourceId());

			//Updating the resource details
			objectToUpdate.setResourceName(resourceModel.getResourceName());
			objectToUpdate.setCapacity(resourceModel.getCapacity());

			//committing the transaction
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}
	}

	/**
	 * Following function helps in adding a resource admin/admins to the selected resource ID.
	 * @param resourceModel(ResourcesModel) contains the information regarding the resource.
	 * @param userModel(UsersModel) contains the information refarding the new resource admins.
	 * @return true/false whether new resource admin/admins are added or not.
	 * @author Arpit Pittie
	 */
	public boolean addResourceAdmin(ResourcesModel resourceModel,
			UsersModel userModel) {
		//getting session
		Session session = sessionFactory.openSession();

		//starting transaction
		session.beginTransaction();

		Criteria criteria = session.createCriteria(UsersModel.class);

		//Getting the user details
		criteria.add(Restrictions.eq("employeeId", userModel.getEmployeeId()));

		UsersModel objectToUpdate = (UsersModel) criteria.uniqueResult();

		try {
			List<ResourcesModel> resourceAdminList = objectToUpdate
					.getAdminOfResources();
			boolean result = true;
			//Checking if the user already has the given resource in his resource admin list
			for (ResourcesModel resourcesModel : resourceAdminList) {
				if (resourcesModel.getResourceId() == resourceModel
						.getResourceId()) {
					result = false;
				}
			}
			//Updating the user role and the admin list
			if (result) {
				objectToUpdate.getAdminOfResources().add(resourceModel);
				objectToUpdate.setRole("res_admin");
			}

			//Saving the updates
			session.saveOrUpdate(objectToUpdate);
			
			//committing transaction
			session.getTransaction().commit();
			return true;
		} catch (NonUniqueObjectException e) {
			e.printStackTrace();
			
			//The resource object is not unique in the given session
			session.merge(objectToUpdate);
			
			//committing transaction
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}
	}

	/**
	 * Following function helps in deleting the resource admin/admins of the selected resource ID.
	 * @param resourceModel(ResourcesModel) contains the information of the resource.
	 * @return true/false whether new resource admin/admins are deleted or not. 
	 * @author Arpit Pittie
	 */
	public boolean deleteResourceAdmin(ResourcesModel resourceModel) {
		//getting session
		Session session = sessionFactory.openSession();

		try {
			//starting transaction
			session.beginTransaction();

			ResourcesModel objectToUpdate = (ResourcesModel) session.get(
					ResourcesModel.class, resourceModel.getResourceId());

			List<UsersModel> listOfAdmin = objectToUpdate.getResourceAdmins();
			List<UsersModel> updatedListOfAdmin = resourceModel
					.getResourceAdmins();

			boolean flag;
			UsersModel userModel;

			for (int i = 0; i < listOfAdmin.size(); i++) {
				userModel = listOfAdmin.get(i);
				flag = true;

				//Checking if a user has been removed from resource admin's list
				for (UsersModel newUsersModel : updatedListOfAdmin) {
					if (newUsersModel.getEmployeeId() == userModel
							.getEmployeeId()) {
						flag = false;
						break;
					}
				}

				if (flag) {
					userModel.getAdminOfResources().remove(objectToUpdate);
					//Checking if the user has other resources as admin
					if (userModel.getAdminOfResources().size() == 0) {
						userModel.setRole("user");
					}
					session.saveOrUpdate(userModel);
				}
			}

			//Committing the transaction
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return true;
		}
	}

}