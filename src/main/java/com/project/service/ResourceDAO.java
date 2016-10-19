package com.project.service;

/**
 *  To perform database interaction for only users
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
	 */
	@SuppressWarnings("unchecked")
	public List<ResourcesModel> allResourceList() {

		Session session = sessionFactory.openSession();
	
		// Getting the result
		return session.createCriteria(ResourcesModel.class).list();
		
	}


	/**
	 * Following function helps in creating a new resource. 
	 * @param resourcesModel contains the information of the new resource.
	 * @return true/false whether resource created successfully or not.
	 */
	public boolean createResource(ResourcesModel resourceModel) {
		//Creating a new session
				Session session = sessionFactory.openSession();
				
				try {
					//Starting a new transaction
					session.beginTransaction();
					
					//Inserting the Users Details in the database
					session.save(resourceModel);
					
					//Committing the current transaction
					session.getTransaction().commit();
					
					return true;
				} catch (Exception e) {
					session.getTransaction().rollback();
					return false;
				}
	}

	/**
	 * Following function helps in deleting an existing resource. 
	 * @param resourcesModel contains the information of resource to be deleted.
	 * @return true/false whether resource deleted successfully or not.
	 */
	/*public boolean deleteResource(ResourcesModel resourceModel) {
		//Creating a new session
		Session session = sessionFactory.openSession();
		
		try {
			//Starting a new transaction
			session.beginTransaction();
			
			//Inserting the Users Details in the database
			session.delete(resourceModel);
			
			//Committing the current transaction
			session.getTransaction().commit();
			
			return true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			return false;
		}
	}*/

	/**
	 * Following function helps in updating the existing resource. 
	 * @param resourcesModel contains the information of the resource to be updated.
	 * @return true/false whether resource updated successfully or not.
	 */
	public boolean editResource(ResourcesModel resourceModel) {
		//Creating a new session
				Session session = sessionFactory.openSession();
				
				try {
					//Starting a new transaction
					session.beginTransaction();
					
						System.out.println("begin");
						System.out.println("resourceMdel  "+resourceModel);
						
					List<UsersModel> usersModel = (List<UsersModel>)resourceModel.getResourceAdmins();
					
					System.out.println("getting users and "+usersModel);
					
					//Inserting the Users Details in the database
					session.update(resourceModel);
					
					System.out.println("updating resourceModel");
					
					UsersModel usersModel1;
					System.out.println("Size of usersmodel "+usersModel.size());
					
					List<ResourcesModel> adminOfResources;
					
					
					for (UsersModel usersModel2 : usersModel) {
						
						
						
						System.out.println("updating resourceModel 1");
						adminOfResources = usersModel2.getAdminOfResources();
						System.out.println("updating resourceModel 2");
						usersModel1 = (UsersModel) session.get(UsersModel.class, usersModel2.getEmployeeId());
						System.out.println("updating resourceModel 3");
						usersModel1.setRole("res_admin");
						System.out.println("updating resourceModel 4");
						
						adminOfResources.add(resourceModel);
						System.out.println("updating resourceModel 5");
						usersModel1.setAdminOfResources(adminOfResources);
						System.out.println("updating resourceModel 6");
						//session.update(usersModel2);
						
					}
					
					System.out.println("updating usersModel");
					
					//Committing the current transaction
					session.getTransaction().commit();
					System.out.println("commit done");
					
					return true;
				} catch (Exception e) {
					session.getTransaction().rollback();
					System.out.println("getting error");
					return false;
				}
	}

	public boolean createResourceAdmin(ResourcesModel resourceModel,
			UsersModel usersModel) {
		
		Session session = sessionFactory.openSession();

		
		try{
			//Starting a new transaction
			session.beginTransaction();
			
			
			
			ResourcesModel updateResource = (ResourcesModel) session.get(ResourcesModel.class, resourceModel.getResourceId());
			List<UsersModel> resourceAdmins = updateResource.getResourceAdmins();
			resourceAdmins.add(usersModel);
			
			//DEBUG
			updateResource.setResourceAdmins(resourceAdmins);
			
			/*UsersModel updateUsers = (UsersModel) session.get(UsersModel.class, usersModel.getEmail());
			List<ResourcesModel> userAsAdminForResources = updateUsers.getAdminOfResources();
			userAsAdminForResources.add(resourceModel);
			*/
			//DEBUG
			/*updateUsers.setAdminOfResources(userAsAdminForResources);*/
			
			System.out.println("started 2");
			//DEBUG
			session.getTransaction().commit();
			
			System.out.println("End");	
			//DEBUG
			return true;
			
		} catch (Exception e) 
			{
			session.getTransaction().rollback();
			
			return false;
			}
		
	}

}