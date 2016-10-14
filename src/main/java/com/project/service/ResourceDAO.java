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
	 * @return the list of all available resources.
	 */
	@SuppressWarnings("unchecked")
	public List<ResourcesVO> allResourceList() {

		Session session = this.sessionFactory.getCurrentSession();

		// Getting the result
		return session.createCriteria(ResourcesModel.class).list();
	}

}
