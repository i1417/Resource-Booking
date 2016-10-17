package com.project.service;

/**
 *  To perform database interaction for only users
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

import javax.transaction.Transactional;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.model.ResourcesModel;
import com.project.model.ResourcesVO;

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

		Session session = this.sessionFactory.getCurrentSession();

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
	public boolean deleteResource(ResourcesModel resourceModel) {
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
	}

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
					
					//Inserting the Users Details in the database
					session.update(resourceModel);
					
					//Committing the current transaction
					session.getTransaction().commit();
					
					return true;
				} catch (Exception e) {
					session.getTransaction().rollback();
					return false;
				}
	}

}