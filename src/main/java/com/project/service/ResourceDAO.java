package com.project.service;

/**
 *  To perform database interaction for only users
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.model.BookingsModel;
import com.project.model.ResourcesModel;
import com.project.model.ResourcesVO;
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
	 * Following function helps in creating a new resource.
	 * 
	 * @param resourcesModel
	 *            contains the information of the new resource.
	 * @return true/false whether resource created successfully or not.
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
			session.getTransaction().rollback();
			return false;
		}
	}

	/**
	 * Following function helps in deleting an existing resource.
	 * 
	 * @param resourcesModel
	 *            contains the information of resource to be deleted.
	 * @return true/false whether resource deleted successfully or not.
	 */
	/*
	 * public boolean deleteResource(ResourcesModel resourceModel) { //Creating
	 * a new session Session session = sessionFactory.openSession();
	 * 
	 * try { //Starting a new transaction session.beginTransaction();
	 * 
	 * //Inserting the Users Details in the database
	 * session.delete(resourceModel);
	 * 
	 * //Committing the current transaction session.getTransaction().commit();
	 * 
	 * return true; } catch (Exception e) { session.getTransaction().rollback();
	 * return false; } }
	 */

	/**
	 * Following function helps in updating the existing resource.
	 * 
	 * @param resourcesModel
	 *            contains the information of the resource to be updated.
	 * @return true/false whether resource updated successfully or not.
	 */
	public boolean editResource(ResourcesModel resourceModel) {

		// Creating a new session
		Session session = sessionFactory.openSession();

		try {
			// Starting a new transaction
			session.beginTransaction();

			// fetching user list from resource model;
			// Users who are resource_admins to this particular resource
			List<UsersModel> usersModel = (List<UsersModel>) resourceModel
					.getResourceAdmins();

			// getting resourceModel from DataBase
			ResourcesModel resourcesModelDataBase = (ResourcesModel) session
					.get(ResourcesModel.class, resourceModel.getResourceId());

			// user update
			// if resource have any resource_admin
			// list of users to be assign as res_admin for this resource
			if (usersModel.size() != 0) {

				System.out.println("begin"); // DEBUG
				System.out.println("resourceMdel  " + resourceModel); // DEBUG
				System.out.println("getting users and " + usersModel); // DEBUG
				System.out.println("updating resourceModel"); // DEBUG
				System.out.println("Size of usersmodel " + usersModel.size()); // DEBUG

				List<ResourcesModel> adminOfResources;

				// getting individual resource admin
				for (UsersModel usersModel2 : usersModel) {

					System.out.println("updating resourceModel 1");

					System.out.println("updating resourceModel 2");

					// getting res_admin from database by employeeId
					UsersModel usersModelDB = (UsersModel) session.get(
							UsersModel.class, usersModel2.getEmployeeId());

					System.out.println("updating resourceModel 3");

					// checking for role
					if (usersModelDB.getRole().equals("user"))
						usersModelDB.setRole("res_admin");

					System.out.println("updating resourceModel 4");

					// getting resource for res_admin
					adminOfResources = usersModelDB.getAdminOfResources();

					// adding new resource to list for res_admin
					boolean flag = true;
					for (ResourcesModel resourcesModelLocal : adminOfResources) {

						if (resourcesModelLocal.getResourceId() == resourceModel
								.getResourceId()) {
							flag = false;
						}

					}
					// if resource is not in res_admin relation for that user
					if (flag) {

						adminOfResources.add(resourceModel);
					}

					// setting new list of resources for res_admin
					usersModelDB.setAdminOfResources(adminOfResources);

					// updating Object
					session.update(usersModelDB);
					System.out.println("updating resourceModel 6");
					// session.update(usersModel2);

				}

			} else {

				// getting res_admin from database for specific resource
				List<UsersModel> usersModelList = resourcesModelDataBase
						.getResourceAdmins();

				System.out.println("users from database for resource "
						+ usersModelList.size()); // DEBUG

				List<ResourcesModel> adminOfResources;

					/*
					 * deleting resource from user Model in res_admin relation
					 * */
				for (UsersModel usersModelLocal : usersModelList) {

					System.out.println("updating resourceModel 1");
					
					// getting userModel from database by emloyeeId
					UsersModel usersModelDataBase = (UsersModel) session.get(
							UsersModel.class, usersModelLocal.getEmployeeId());

					System.out.println("updating resourceModel 2");
					
					//getting resources list for which user is admin
					adminOfResources = usersModelDataBase.getAdminOfResources();

					if (adminOfResources.size() <= 1) {

						System.out.println("updating resourceModel 3");
						usersModelDataBase.setRole("user");
						System.out.println("updating resourceModel 4");

					}

					//removing resource from user list
					adminOfResources.remove(resourceModel);
					
					usersModelDataBase.setAdminOfResources(adminOfResources);
					
					// updated list resources for user
					session.update(usersModelDataBase);
				}

			}
			
			
			//updating users(admin) list of resource
			resourcesModelDataBase.setResourceAdmins(resourceModel.getResourceAdmins());
			resourcesModelDataBase.setCapacity(resourceModel.getCapacity());
			resourcesModelDataBase.setResourceName(resourceModel.getResourceName());

			// Resource Update

			// Inserting the Users Details in the database
			session.update(resourcesModelDataBase);

			System.out.println("updating usersModel");

			// Committing the current transaction
			session.getTransaction().commit();
			System.out.println("commit done");

			return true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			System.out.println("getting error");
			return false;
		}
	}

}
