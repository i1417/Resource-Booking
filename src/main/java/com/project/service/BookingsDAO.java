package com.project.service;

/**
 *  To perform database interaction for only users
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

import javax.transaction.Transactional;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.model.BookingsModel;

@Repository("bookingsDAO")
@Transactional
public class BookingsDAO {

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
	 * get all the pending bookings )
	 * 
	 * @return List of bookings having status = pending
	 */
	/*public List<BookingsVO> pendingBookingsListById(Integer resourceId) {

		Session session = this.sessionFactory.getCurrentSession();

		Criteria cr = session.createCriteria(BookingsModel.class);
		cr.add(Restrictions.and();
		cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		@SuppressWarnings("unchecked")
		List<BookingsVO> results = cr.list();
		System.out.println(results.size());
		// Getting the result
		return results;
	}
*/
	
	public List<BookingsModel> pendingBookingsListById(BookingsModel bookings) {

		Session session = this.sessionFactory.getCurrentSession();

		Criteria cr = session.createCriteria(BookingsModel.class);
		cr.add(Restrictions.and(Restrictions.eq("status","pending"),
				Restrictions.eq("resourceDetails",bookings.getResourceDetails())));
		cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		@SuppressWarnings("unchecked")
		List<BookingsModel> results = cr.list();
		System.out.println(results.size());
		// Getting the result
		return results;
	}

	/**
	 * Following function fires query to database to get the required result(i.e
	 * get all the pending bookings )
	 * 
	 * @return List of bookings having status = pending
	 */
	public List<BookingsModel> approvedBookingsList() {

		Session session = this.sessionFactory.getCurrentSession();

		Criteria cr = session.createCriteria(BookingsModel.class);
//		cr = cr.createAlias("userDetails", "userDetails");
//		cr = cr.createAlias("resourceDetails", "resourceDetails");
		/*cr = cr.setProjection(Projections.projectionList()
				.add(Projections.property("bookingId"), "bookingId")
				.add(Projections.property("date"), "date")
				.add(Projections.property("startTime"), "startTime")
				.add(Projections.property("endTime"), "endTime")
				.add(Projections.property("title"), "title")
				.add(Projections.property("description"), "description")
				.add(Projections.property("status"), "status")
				.add(Projections.property("numberOfParticipants"), "numberOfParticipants")
//				.add(Projections.property("userDetails"), "userDetails")
				.add(Projections.property("userDetails.employeeId"), "userDetails.employeeId")
				.add(Projections.property("userDetails.name"), "userDetails.name")
				.add(Projections.property("userDetails.email"), "userDetails.email"));*/
//				.add(Projections.property("resourceDetails"), "resourceDetails")
//				.add(Projections.property("resourceDetails.resourceId"), "resourceDetails.resourceId")
//				.add(Projections.property("resourceDetails.resourceName"), "resourceDetails.resourceName")
//				.add(Projections.property("resourceDetails.type"), "resourceDetails.type")
//				.add(Projections.property("resourceDetails.capacity"), "resourceDetails.capacity"));
//			.setResultTransformer(Transformers.aliasToBean(BookingsModel.class));
		
		// Getting the current date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String currentDate = dateFormat.format(cal.getTime());
		System.out.println(currentDate);Date date;
		
		 try {

	             date = dateFormat.parse(currentDate);
	            System.out.println(date);					//DEBUG
	            cr.add(Restrictions.and(Restrictions.ge("date",date),Restrictions.eq("status", "Approved")));
	            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
		 
//		cr.add(Restrictions.and(Restrictions.ge("date",date),Restrictions.eq("status", "Approved")));
		@SuppressWarnings("unchecked")
		List<BookingsModel> results = cr.list();
		System.out.println(results.size());					//DEBUG
		System.out.println(results+"DAO result");
		// Getting the result
		return results;
		
		
	}
}
