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
import com.project.model.BookingsModel;
import com.project.model.UsersModel;

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
	/*
	 * public List<BookingsVO> pendingBookingsListById(Integer resourceId) {
	 * 
	 * Session session = this.sessionFactory.getCurrentSession();
	 * 
	 * Criteria cr = session.createCriteria(BookingsModel.class);
	 * cr.add(Restrictions.and();
	 * cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	 * 
	 * @SuppressWarnings("unchecked") List<BookingsVO> results = cr.list();
	 * System.out.println(results.size()); // Getting the result return results;
	 * }
	 */
	/**
	 * Following function fetches the list of all pending bookings corresponding
	 * to specific resource ID
	 * 
	 * @param bookings
	 *            is a object of BookingsModel.
	 * @return the list of all pending bookings corresponding to specific
	 *         resource ID
	 */
	public List<BookingsModel> pendingBookingsListById(BookingsModel bookings) {

		Session session = this.sessionFactory.getCurrentSession();

		Criteria cr = session.createCriteria(BookingsModel.class);
		cr.add(Restrictions.and(
				Restrictions.eq("status", "pending"),
				Restrictions.eq("resourceDetails",
						bookings.getResourceDetails())));
		cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		@SuppressWarnings("unchecked")
		List<BookingsModel> results = cr.list();
		System.out.println(results.size());
		// Getting the result
		return results;
	}

	/**
	 * Following function fires query to database to get the required result(i.e
	 * get all the Approved bookings )
	 * 
	 * @return List of bookings having status = Approved
	 */
	public List<BookingsModel> approvedBookingsList() {

		Session session = this.sessionFactory.getCurrentSession();

		Criteria cr = session.createCriteria(BookingsModel.class);
		// cr = cr.createAlias("userDetails", "userDetails");
		// cr = cr.createAlias("resourceDetails", "resourceDetails");
		/*
		 * cr = cr.setProjection(Projections.projectionList()
		 * .add(Projections.property("bookingId"), "bookingId")
		 * .add(Projections.property("date"), "date")
		 * .add(Projections.property("startTime"), "startTime")
		 * .add(Projections.property("endTime"), "endTime")
		 * .add(Projections.property("title"), "title")
		 * .add(Projections.property("description"), "description")
		 * .add(Projections.property("status"), "status")
		 * .add(Projections.property("numberOfParticipants"),
		 * "numberOfParticipants") // .add(Projections.property("userDetails"),
		 * "userDetails") .add(Projections.property("userDetails.employeeId"),
		 * "userDetails.employeeId")
		 * .add(Projections.property("userDetails.name"), "userDetails.name")
		 * .add(Projections.property("userDetails.email"),
		 * "userDetails.email"));
		 */
		// .add(Projections.property("resourceDetails"), "resourceDetails")
		// .add(Projections.property("resourceDetails.resourceId"),
		// "resourceDetails.resourceId")
		// .add(Projections.property("resourceDetails.resourceName"),
		// "resourceDetails.resourceName")
		// .add(Projections.property("resourceDetails.type"),
		// "resourceDetails.type")
		// .add(Projections.property("resourceDetails.capacity"),
		// "resourceDetails.capacity"));
		// .setResultTransformer(Transformers.aliasToBean(BookingsModel.class));

		// Getting the current date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		// converting date type data to String format
		String currentDate = dateFormat.format(cal.getTime());
		System.out.println(currentDate); // DEBUG
		Date date;

		try {
			// converting string data to date
			date = dateFormat.parse(currentDate);

			System.out.println(date); // DEBUG
			// checking for both date and status
			cr.add(Restrictions.and(Restrictions.ge("date", date),
					Restrictions.eq("status", "Approved")));
			cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		// cr.add(Restrictions.and(Restrictions.ge("date",date),Restrictions.eq("status",
		// "Approved")));
		@SuppressWarnings("unchecked")
		List<BookingsModel> results = cr.list();

		System.out.println(results.size()); // DEBUG
		System.out.println(results + "DAO result");
		// Getting the result
		return results;

	}

	/**
	 * Following function updates the status of bookings(accepted/cancelled)
	 * @param bookingsModel contains the information related to the booking status and other attributes 
	 * @return true/false whether booking status has been updated successfully.
	 */
	public boolean updateBookingsStatus(BookingsModel bookingsModel) {
		Session session = sessionFactory.openSession();

		
		
		
		
		System.out.println("booking Model "+ bookingsModel);	//DEBUG
		try{
			//Starting a new transaction
			session.beginTransaction();
			
			String status = bookingsModel.getStatus();
			
			BookingsModel objectToUpdate = (BookingsModel) session.get(BookingsModel.class, bookingsModel.getBookingId());
			
			
			//DEBUG
			objectToUpdate.setStatus(status);
			
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