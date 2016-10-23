/**
 * To perform database interaction.
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
	 * 
	 * @return the list of all available resources .
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
	 * @return true/false whether resource created successfully or not.
	 */
	public boolean createResource(ResourcesModel resourceModel) {
		// Creating a new session
		Session session = sessionFactory.openSession();
		System.out.println("resmodel"+resourceModel);

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
	 * @param resourcesModel(ResourcesModel) contains the information of the resource to be edited.
	 * @return true/false whether resource edited successfully or not.
	 */
	public boolean editResource(ResourcesModel resourceModel) {
		//getting session
		Session session = sessionFactory.openSession();

		try {
			//starting a transaction
			session.beginTransaction();

			ResourcesModel objectToUpdate = (ResourcesModel) session.get(
					ResourcesModel.class, resourceModel.getResourceId());

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
	 */
	public boolean addResourceAdmin(ResourcesModel resourceModel,
			UsersModel userModel) {
		//getting session
		Session session = sessionFactory.openSession();
		//starting transaction
		session.beginTransaction();
		//using Criteria Query
		Criteria criteria = session.createCriteria(UsersModel.class);

		criteria.add(Restrictions.eq("employeeId", userModel.getEmployeeId()));

		UsersModel objectToUpdate = (UsersModel) criteria.uniqueResult();

		try {
			List<ResourcesModel> resourceAdminList = objectToUpdate
					.getAdminOfResources();
			boolean result = true;
			for (ResourcesModel resourcesModel : resourceAdminList) {
				if (resourcesModel.getResourceId() == resourceModel
						.getResourceId()) {
					result = false;
				}
			}
			if (result) {
				objectToUpdate.getAdminOfResources().add(resourceModel);
				objectToUpdate.setRole("res_admin");
			}

			session.saveOrUpdate(objectToUpdate);
			//committing transaction
			session.getTransaction().commit();
			return true;
		} catch (NonUniqueObjectException e) {
			e.printStackTrace();
			session.merge(objectToUpdate);
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

				for (UsersModel newUsersModel : updatedListOfAdmin) {
					if (newUsersModel.getEmployeeId() == userModel
							.getEmployeeId()) {
						flag = false;
						break;
					}
				}

				if (flag) {
					System.out.println("CAlled Remove admin");
					userModel.getAdminOfResources().remove(objectToUpdate);
					if (userModel.getAdminOfResources().size() == 0) {
						userModel.setRole("user");
					}
					session.saveOrUpdate(userModel);
					System.out.println(userModel);
				} else {
					// i++;
					System.out.println("Calling add admin");
					// objectToUpdate.getResourceAdmins().add(userModel);
				}
			}

			//System.out.println("List" + objectToUpdate.getResourceAdmins());

			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return true;
		}
	}

}
