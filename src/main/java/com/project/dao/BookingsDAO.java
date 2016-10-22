/**
 * To perform database interaction for only users
 * @author Pratap Singh Ranawat and Vivek Mittal
 */
package com.project.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import com.project.model.BookingsModel;
import com.project.model.UsersModel;
import com.project.model.UsersVO;
import com.project.service.MailService;

@Repository("bookingsDAO")
@Transactional
public class BookingsDAO {

	// To create a session for the database operation
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MailService mailService;

	// To get the beans
	@Autowired
	private ApplicationContext context;

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
	 * Following function fetches the list of all pending bookings corresponding
	 * to particular employeeID
	 * 
	 * @param bookings
	 *            is a object of BookingsModel.
	 * @return the list of all pending bookings corresponding to specific
	 *         resource ID
	 * @throws ParseException
	 */
	public List<BookingsModel> pendingBookingsListByEmployeeId(
			UsersModel usersModel) {

		Session session = this.sessionFactory.getCurrentSession();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String currentDate = dateFormat.format(cal.getTime());
		Date date = null;
		try {
			date = dateFormat.parse(currentDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Criteria cr = session.createCriteria(BookingsModel.class);
		cr.add(Restrictions.and(Restrictions.ge("date", date),
				Restrictions.eq("status", "pending"),
				Restrictions.eq("userDetails", usersModel)));
		cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		@SuppressWarnings("unchecked")
		List<BookingsModel> results = cr.list();
		// Getting the result
		return results;
	}

	/**
	 * Following function fires query to database to get the required result(i.e
	 * get all the Approved bookings )
	 * 
	 * @return List of bookings having status = Approved
	 */
	public List<BookingsModel> approvedBookingsList(UsersModel userModel) {

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
					Restrictions.eq("status", "Approved"),Restrictions.ne("userDetails", userModel)));
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
	 * 
	 * @param bookingsModel
	 *            contains the information related to the booking status and
	 *            other attributes
	 * @return true/false whether booking status has been updated successfully.
	 */
	public boolean updateBookingsStatus(BookingsModel bookingsModel) {
		Session session = sessionFactory.openSession();

		System.out.println("booking Model " + bookingsModel); // DEBUG
		try {
			// Starting a new transaction
			session.beginTransaction();

			String status = bookingsModel.getStatus();

			BookingsModel objectToUpdate = (BookingsModel) session.get(
					BookingsModel.class, bookingsModel.getBookingId());

			// DEBUG
			objectToUpdate.setStatus(status);

			// DEBUG
			session.getTransaction().commit();

			System.out.println("End");
			// DEBUG
			return true;

		} catch (Exception e) {
			session.getTransaction().rollback();

			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean updateBookingsStatusApproved(BookingsModel bookingsModel) {
		Session session = sessionFactory.openSession();

		System.out.println("I am here");
		try {
			// Starting a new transaction
			session.beginTransaction();

			String status = bookingsModel.getStatus();
			
			Criteria criteria = session.createCriteria(BookingsModel.class);
			BookingsModel objectToUpdate;

			bookingsModel = (BookingsModel) session.get(BookingsModel.class, bookingsModel.getBookingId());
			
			criteria.add(Restrictions.and(Restrictions.eq("date",
					bookingsModel.getDate()), Restrictions.eq("status",
					"approved"), Restrictions.or(
					Restrictions.between("startTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.between("endTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.and(Restrictions.ge("endTime",
							bookingsModel.getEndTime()),Restrictions.le("startTime", bookingsModel.getStartTime())
					))));
			
			List<BookingsModel> forStatus = criteria.list();
			System.out.println(forStatus.size()+"forStatus");
			if(forStatus.size() != 0) {
				for (BookingsModel bookings : forStatus) {
					
					objectToUpdate = (BookingsModel) session.get(
							BookingsModel.class, bookings.getBookingId());
					objectToUpdate.setStatus("Rejected");
					session.update(objectToUpdate);
					
					String mailMessage = "Dear "+bookings.getUserDetails().getName()+"\nYour booking with ID : "
							+ bookings.getBookingId()
							+ "\nThe current booking status is : "
							+ bookings.getStatus()
							+ "\n\nRegards\nResource Booking Team";
					
					UsersVO user = context.getBean(UsersVO.class);
					BeanUtils.copyProperties(bookings.getUserDetails(), user);
					mailService.sendHTMLMail(user,
							"Booking Status Changed", mailMessage);
				}
			}
			
			objectToUpdate = (BookingsModel) session.get(
					BookingsModel.class, bookingsModel.getBookingId());

			// DEBUG
			objectToUpdate.setStatus(status);

			// DEBUG
			session.getTransaction().commit();

			System.out.println("End");
			// DEBUG
			return true;

		} catch (Exception e) {
			session.getTransaction().rollback();

			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public BookingsModel createBooking(BookingsModel bookingsModel) {
		Session session = sessionFactory.openSession();

		try {
			session.beginTransaction();

			Criteria criteria = session.createCriteria(BookingsModel.class);
			criteria.add(Restrictions.eq("date", bookingsModel.getDate()));
			List<BookingsModel> forStatus = criteria.list();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String dt = dateFormat.format(bookingsModel.getDate());

			String idForBooking = bookingsModel.getResourceDetails()
					.getResourceName() + dt + "-" + (forStatus.size() + 1);

			bookingsModel.setBookingId(idForBooking);

			criteria = session.createCriteria(BookingsModel.class);

			criteria.add(Restrictions.and(Restrictions.eq("date",
					bookingsModel.getDate()), Restrictions.eq("status",
					"approved"), Restrictions.or(
					Restrictions.between("startTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.between("endTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.and(Restrictions.ge("endTime",
							bookingsModel.getEndTime()),Restrictions.le("startTime", bookingsModel.getStartTime())
					))));

			forStatus = criteria.list();

			if (forStatus.size() == 0) {
				bookingsModel.setStatus("Approved");
			} else {
				UsersVO user = context.getBean(UsersVO.class);

				// dateFormat = new SimpleDateFormat("yyyy/MM/dd");

				for (BookingsModel bookings : forStatus) {
					String mailMessage = "<p>Dear "
							+ bookings.getUserDetails().getName()
							+ "</p>"
							+ "<p>Your booking with ID : "
							+ bookings.getBookingId()
							+ "</p>"
							+ "<p>Date : "
							+ bookings.getDate()
							+ "</p>"
							+ "<p>Start Time : "
							+ bookings.getStartTime()
							+ "</p>"
							+ "<p>End Time : "
							+ bookings.getEndTime()
							+ "</p>"
							+ "<br/><p>There is a request for another booking made by : "
							+ bookingsModel.getUserDetails().getName()
							+ "<p>Title : "
							+ bookingsModel.getTitle()
							+ "</p>"
							+ "<p>Description : "
							+ bookingsModel.getDescription()
							+ "</p>"
							+ "<br/><p>Do you want to cancel your request and approve it?</p><br/>"
							+ "<div style='display: inline-block; background-color:#5cb85c; padding: 8px 15px; border-radius:5px; margin:8px 15px; border:1px solid white'>"
							+ "<a style='color: white; text-decoration: none;' href='http://localhost:8080/Project-Authentication/statusChange.html?bookingId="
							+ bookings.getBookingId() + "&newBookingId="
							+ idForBooking + "&status=Accepted' "
							+ "value='Accepted' id='status'>Accept</a>"
							+ "</div>";

					BeanUtils.copyProperties(bookings.getUserDetails(), user);

					mailService.sendHTMLMail(user, "Booking Request from "
							+ bookingsModel.getUserDetails().getName(),
							mailMessage);
				}

				bookingsModel.setStatus("Pending");
			}

			session.save(bookingsModel);
			session.getTransaction().commit();

			return bookingsModel;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
	}

	/* edit booking request */
	@SuppressWarnings("unchecked")
	public BookingsModel editBooking(BookingsModel bookingsModel) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			BookingsModel bookingsModelDB = (BookingsModel) session.get(
					BookingsModel.class, bookingsModel.getBookingId());

			System.out.println("getting ojbect from db");
			bookingsModelDB.setDate(bookingsModel.getDate());
			bookingsModelDB.setStartTime(bookingsModel.getStartTime());
			bookingsModelDB.setEndTime(bookingsModel.getEndTime());
			bookingsModelDB.setNumberOfParticipants(bookingsModel
					.getNumberOfParticipants());
			bookingsModelDB.setTitle(bookingsModel.getTitle());
			bookingsModelDB.setDescription(bookingsModel.getDescription());

			Criteria criteria = session.createCriteria(BookingsModel.class);

			criteria.add(Restrictions.and(Restrictions.eq("date",
					bookingsModel.getDate()), Restrictions.eq("status",
					"approved"), Restrictions.ne("bookingId", bookingsModel.getBookingId()), Restrictions.or(
					Restrictions.between("startTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.between("endTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.and(Restrictions.ge("endTime",
							bookingsModel.getEndTime()),Restrictions.le("startTime", bookingsModel.getStartTime())
					))));

			List<BookingsModel> forStatus = criteria.list();
			if (forStatus.size() == 0) {
				bookingsModelDB.setStatus("Approved");
			} else {
				bookingsModelDB.setStatus("Pending");
			}
			System.out.println("updating records into db");
			session.update(bookingsModelDB);
			System.out.println("transaction end");

			session.getTransaction().commit();
			System.out.println("transaction committed");
			return bookingsModelDB;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			System.out.println("transaction roll back");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean userBookingStatusChange(String bookingId,
			String newBookingId, String status) {
		Session session = sessionFactory.openSession();

		try {
			session.beginTransaction();

			BookingsModel oldBooking = (BookingsModel) session.get(
					BookingsModel.class, bookingId);
			BookingsModel newBooking = (BookingsModel) session.get(
					BookingsModel.class, newBookingId);

			if (newBooking.getStatus().equalsIgnoreCase("pending")) {
				oldBooking.setStatus("Cancelled");

				String mailMessage = "Your booking with ID : "
						+ oldBooking.getBookingId()
						+ "\nThe current booking status is : "
						+ oldBooking.getStatus()
						+ "\n\nRegards\nResource Booking Team";

				UsersVO user = context.getBean(UsersVO.class);
				BeanUtils.copyProperties(oldBooking.getUserDetails(), user);

				mailService.sendHTMLMail(user, "Booking Status Changed",
						mailMessage);

				Criteria criteria = session.createCriteria(BookingsModel.class);

				criteria.add(Restrictions.and(Restrictions.eq("date",
						newBooking.getDate()), Restrictions.eq("status",
						"approved"), Restrictions.or(
						Restrictions.between("startTime",
								newBooking.getStartTime(),
								newBooking.getEndTime()),
						Restrictions.between("endTime",
								newBooking.getStartTime(),
								newBooking.getEndTime()),
						Restrictions.and(Restrictions.ge("endTime",
								newBooking.getEndTime()),Restrictions.le("startTime", newBooking.getStartTime())
						))));

				List<BookingsModel> forStatus = criteria.list();
				if (forStatus.size() == 0) {
					newBooking.setStatus("Approved");
					mailMessage = "Your booking with ID : "
							+ newBooking.getBookingId()
							+ "\nThe current booking status is : "
							+ newBooking.getStatus()
							+ "\n\nRegards\nResource Booking Team";

					BeanUtils.copyProperties(newBooking.getUserDetails(), user);
					mailService.sendHTMLMail(user, "Booking Status Changed",
							mailMessage);
				}
			}

			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean cancelTodayBookings() {
		Session session = sessionFactory.openSession();
		
		try {
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(BookingsModel.class);
			
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();
			
			criteria.add(Restrictions.and(Restrictions.eq("date", date), Restrictions.eq("status", "Pending")));
			
			List<BookingsModel> listToUpdate = criteria.list();
			
			for (BookingsModel bookingsModel : listToUpdate) {
				bookingsModel.setStatus("Cancelled");
				session.update(bookingsModel);
			}
			
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}
	}

}