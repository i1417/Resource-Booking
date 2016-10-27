/**
 * To perform database interaction for the bookings
 * @author Pratap Singh Ranawat and Vivek Mittal
 */
package com.project.dao;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import com.project.model.BookingsModel;
import com.project.model.UsersModel;
import com.project.service.MailService;
import com.project.vo.UsersVO;

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
	 * Following function fetches the list of all pending bookings corresponding
	 * to specific resource ID
	 * 
	 * @param bookings
	 *            (BookingsModel) contains information regarding bookings.
	 * @return the list of all pending bookings corresponding to specific
	 *         resource ID
	 * @author Vivek Mittal, Pratap Singh
	 */
	public List<BookingsModel> pendingBookingsListById(BookingsModel bookings) {
		// getting session
		Session session = this.sessionFactory.getCurrentSession();

		Criteria cr = session.createCriteria(BookingsModel.class);
		// Checking for bookings with pending status.
		cr.add(Restrictions.and(
				Restrictions.eq("status", "pending"),
				Restrictions.eq("resourceDetails",
						bookings.getResourceDetails())));
		// getting the result set containing distinct results.
		cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		@SuppressWarnings("unchecked")
		List<BookingsModel> results = cr.list();

		// Getting the result
		return results;
	}

	/**
	 * Following function fetches the list of all pending bookings corresponding
	 * to particular employeeID
	 * 
	 * @param usersModel
	 *            (UsersModel) contains user details.
	 * @return the list of all pending bookings corresponding to specific
	 *         employee ID
	 * @author Amit Sharma
	 */
	public List<BookingsModel> pendingBookingsListByEmployeeId(
			UsersModel usersModel) {
		// getting session
		Session session = this.sessionFactory.getCurrentSession();

		// Getting the current date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();

		// converting date type data to String format
		String currentDate = dateFormat.format(cal.getTime());
		Date date = null;
		try {
			date = dateFormat.parse(currentDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Criteria cr = session.createCriteria(BookingsModel.class);

		// checking for bookings having status as pending.
		cr.add(Restrictions.and(Restrictions.ge("date", date),
				Restrictions.eq("status", "pending"),
				Restrictions.eq("userDetails", usersModel)));

		// getting the result set containing distinct results.
		cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		@SuppressWarnings("unchecked")
		List<BookingsModel> results = cr.list();
		// Getting the result
		return results;
	}

	/**
	 * Following function fetches the list of all approved bookings
	 * corresponding to particular employeeID
	 * 
	 * @param usersModel
	 *            (UsersModel) contains user details.
	 * @return the list of all approved bookings corresponding to specific
	 *         employee ID
	 * @author Arpit Pittie
	 */
	@SuppressWarnings("unchecked")
	public List<BookingsModel> approvedBookingsListByEmployeeId(
			UsersModel usersModel) {
		// Getting session
		Session session = this.sessionFactory.getCurrentSession();

		Criteria cr = session.createCriteria(BookingsModel.class);
		cr.add(Restrictions.and(Restrictions.eq("status", "approved"),
				Restrictions.eq("userDetails", usersModel)));
		// getting the result set containing distinct results
		cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<BookingsModel> results = cr.list();
		// Getting the result
		return results;
	}

	/**
	 * To get the approved booking of all the other user
	 * 
	 * @param userModel
	 *            - (UsersModel) contains user details whom booking need not to
	 *            be fetched
	 * @return - The list of BookingsModel containing the approved booking for
	 *         all the other users
	 * @author Vivek Mittal, Pratap Singh
	 */
	@SuppressWarnings("unchecked")
	public List<BookingsModel> approvedBookingsListOtherUsers(
			UsersModel userModel) {

		// getting session
		Session session = this.sessionFactory.getCurrentSession();

		Criteria cr = session.createCriteria(BookingsModel.class);

		// Getting the current date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();

		// converting date type data to String type
		String currentDate = dateFormat.format(cal.getTime());
		Date date;

		try {
			// converting string data to date
			date = dateFormat.parse(currentDate);

			// checking for both date and status
			cr.add(Restrictions.and(Restrictions.ge("date", date),
					Restrictions.eq("status", "Approved"),
					Restrictions.ne("userDetails", userModel)));
			cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<BookingsModel> results = cr.list();

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
	 * @author Vivek Mittal, Pratap Singh
	 */
	public boolean updateBookingsStatus(BookingsModel bookingsModel) {
		// getting session
		Session session = sessionFactory.openSession();

		try {
			// Starting a new transaction
			session.beginTransaction();

			// setting the updated status.
			String status = bookingsModel.getStatus();

			BookingsModel objectToUpdate = (BookingsModel) session.get(
					BookingsModel.class, bookingsModel.getBookingId());

			objectToUpdate.setStatus(status);

			// committing transaction
			session.getTransaction().commit();

			return true;

		} catch (Exception e) {
			session.getTransaction().rollback();

			return false;
		}
	}

	/**
	 * To change the status of the booking to approve canceling all the clashed
	 * bookings
	 * 
	 * @param bookingsModel
	 *            - The booking details whom status need to be changed to
	 *            approved
	 * @return - True if the operation is successful else false
	 * @author Arpit Pittie
	 */
	@SuppressWarnings("unchecked")
	public boolean updateBookingsStatusApproved(BookingsModel bookingsModel) {
		// Getting Session
		Session session = sessionFactory.openSession();

		try {
			// Starting a new transaction
			session.beginTransaction();
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String currentTime = dateFormat.format(cal.getTime());

			Time startTime = new Time(dateFormat.parse(currentTime).getTime());

			// Checking if the booking start time is less than the current time
			if (startTime.after(bookingsModel.getStartTime())) {
				return false;
			}

			String status = bookingsModel.getStatus();

			Criteria criteria = session.createCriteria(BookingsModel.class);
			BookingsModel objectToUpdate;

			bookingsModel = (BookingsModel) session.get(BookingsModel.class,
					bookingsModel.getBookingId());

			// To get the clashing bookings
			criteria.add(Restrictions.and(Restrictions.eq("resourceDetails",
					bookingsModel.getResourceDetails()), Restrictions.eq(
					"date", bookingsModel.getDate()), Restrictions.eq("status",
					"approved"), Restrictions.or(
					Restrictions.between("startTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.between("endTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.and(
							Restrictions.ge("endTime",
									bookingsModel.getEndTime()),
							Restrictions.le("startTime",
									bookingsModel.getStartTime())))));

			List<BookingsModel> conflictBookingList = criteria.list();

			// To cancel all the bookings which are in clash
			if (conflictBookingList.size() != 0) {
				for (BookingsModel bookings : conflictBookingList) {

					objectToUpdate = (BookingsModel) session.get(
							BookingsModel.class, bookings.getBookingId());
					objectToUpdate.setStatus("Rejected");
					session.update(objectToUpdate);

					// Sending the cancellation mail
					String mailMessage = "Dear "
							+ bookings.getUserDetails().getName()
							+ "\nYour booking with ID : "
							+ bookings.getBookingId()
							+ "\nThe current booking status is : "
							+ bookings.getStatus()
							+ "\n\nRegards\nResource Booking Team";

					UsersVO user = context.getBean(UsersVO.class);
					BeanUtils.copyProperties(bookings.getUserDetails(), user);
					mailService.sendHTMLMail(user, "Booking Status Changed",
							mailMessage);
				}
			}

			objectToUpdate = (BookingsModel) session.get(BookingsModel.class,
					bookingsModel.getBookingId());

			// Updating the status to approve
			objectToUpdate.setStatus(status);

			session.getTransaction().commit();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}
	}

	/**
	 * Following function creates a new booking.
	 * 
	 * @param bookingsModel
	 *            (BookingsModel) contains the information regarding new
	 *            booking.
	 * @return BookingsModel having info regarding booking created.
	 * @author Vivek Mittal, Pratap Singh, Arpit Pittie
	 */
	@SuppressWarnings("unchecked")
	public BookingsModel createBooking(BookingsModel bookingsModel) {
		// getting session
		Session session = sessionFactory.openSession();

		try {
			// starting a new transaction
			session.beginTransaction();

			Criteria criteria = session.createCriteria(BookingsModel.class);

			criteria.add(Restrictions.eq("date", bookingsModel.getDate()));
			List<BookingsModel> conflictBookingList = criteria.list();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String dt = dateFormat.format(bookingsModel.getDate());

			String idForBooking = bookingsModel.getResourceDetails()
					.getResourceName() + dt + "-" + (conflictBookingList.size() + 1);

			bookingsModel.setBookingId(idForBooking);

			criteria = session.createCriteria(BookingsModel.class);

			// To check if there are any booking which is clashing with the new
			// request
			criteria.add(Restrictions.and(Restrictions.eq("resourceDetails",
					bookingsModel.getResourceDetails()), Restrictions.eq(
					"date", bookingsModel.getDate()), Restrictions.eq("status",
					"approved"), Restrictions.or(
					Restrictions.between("startTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.between("endTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.and(
							Restrictions.ge("endTime",
									bookingsModel.getEndTime()),
							Restrictions.le("startTime",
									bookingsModel.getStartTime())))));

			conflictBookingList = criteria.list();

			// The slot is empty and automatic approving the booking
			if (conflictBookingList.size() == 0) {
				bookingsModel.setStatus("Approved");
			} else {
				UsersVO user = context.getBean(UsersVO.class);

				// Sending mail to the user who made a booking at the same time
				// slot
				for (BookingsModel bookings : conflictBookingList) {
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
							+ "<a style='color: white; text-decoration: none;' href='http://localhost:8080/Resource-Booking/statusChange.html?bookingId="
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

			// committing transaction
			session.getTransaction().commit();

			return bookingsModel;
		} catch (NonUniqueObjectException e) {
			e.printStackTrace();
			session.merge(bookingsModel);
			session.getTransaction().commit();
			return bookingsModel;
		} catch (Exception exp) {
			exp.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
	}

	/**
	 * Following function edits the existing booking.
	 * 
	 * @param bookingsModel
	 *            (BookingsModel) contains the information regarding edited
	 *            booking
	 * @return BookingsModel having information regarding edited booking.
	 * @author Vivek Mittal, Pratap Singh
	 */
	@SuppressWarnings("unchecked")
	public BookingsModel editBooking(BookingsModel bookingsModel) {
		// getting session
		Session session = sessionFactory.openSession();
		try {
			// starting a new transaction
			session.beginTransaction();
			BookingsModel bookingsModelDB = (BookingsModel) session.get(
					BookingsModel.class, bookingsModel.getBookingId());

			// Setting all attributes of BookingsModel
			bookingsModelDB.setDate(bookingsModel.getDate());
			bookingsModelDB.setStartTime(bookingsModel.getStartTime());
			bookingsModelDB.setEndTime(bookingsModel.getEndTime());
			bookingsModelDB.setNumberOfParticipants(bookingsModel
					.getNumberOfParticipants());
			bookingsModelDB.setTitle(bookingsModel.getTitle());
			bookingsModelDB.setDescription(bookingsModel.getDescription());

			Criteria criteria = session.createCriteria(BookingsModel.class);

			// To check if there are any booking which is clashing with the
			// updated booking request
			criteria.add(Restrictions.and(Restrictions.eq("resourceDetails",
					bookingsModel.getResourceDetails()), Restrictions.eq(
					"date", bookingsModel.getDate()), Restrictions.eq("status",
					"approved"), Restrictions.ne("bookingId",
					bookingsModel.getBookingId()), Restrictions.or(
					Restrictions.between("startTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.between("endTime",
							bookingsModel.getStartTime(),
							bookingsModel.getEndTime()),
					Restrictions.and(
							Restrictions.ge("endTime",
									bookingsModel.getEndTime()),
							Restrictions.le("startTime",
									bookingsModel.getStartTime())))));

			List<BookingsModel> conflictBookingList = criteria.list();
			if (conflictBookingList.size() == 0) {
				bookingsModelDB.setStatus("Approved");
			} else {
				bookingsModelDB.setStatus("Pending");
			}

			session.update(bookingsModelDB);

			// committing transaction
			session.getTransaction().commit();

			return bookingsModelDB;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return null;
		}
	}

	/**
	 * To update the status of the first user approved booking to cancelled and
	 * the new user pending booking status to approve if there aren't any more
	 * clashes
	 * 
	 * @param bookingId
	 *            - The booking id to be cancelled
	 * @param newBookingId
	 *            - The booking id to try to approve
	 * @param status
	 *            - The status to which the new booking is to be changed
	 * @return - True if the operation is successful else false
	 * @author Arpit Pittie
	 */
	@SuppressWarnings("unchecked")
	public boolean userBookingStatusChange(String bookingId,
			String newBookingId, String status) {
		// getting session
		Session session = sessionFactory.openSession();

		try {
			// starting a new transaction
			session.beginTransaction();

			BookingsModel oldBooking = (BookingsModel) session.get(
					BookingsModel.class, bookingId);
			BookingsModel newBooking = (BookingsModel) session.get(
					BookingsModel.class, newBookingId);

			// Checking if the booking has already been approved or not
			if (newBooking.getStatus().equalsIgnoreCase("pending")) {
				// Cancelling the old booking
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

				// Checking if there are any more clashes with the booking
				criteria.add(Restrictions.and(Restrictions.eq(
						"resourceDetails", newBooking.getResourceDetails()),
						Restrictions.eq("date", newBooking.getDate()),
						Restrictions.eq("status", "approved"), Restrictions.or(
								Restrictions.between("startTime",
										newBooking.getStartTime(),
										newBooking.getEndTime()), Restrictions
										.between("endTime",
												newBooking.getStartTime(),
												newBooking.getEndTime()),
								Restrictions.and(
										Restrictions.ge("endTime",
												newBooking.getEndTime()),
										Restrictions.le("startTime",
												newBooking.getStartTime())))));

				List<BookingsModel> conflictBookingList = criteria.list();
				// There are no more clashes hence updating the status of
				// request to approve
				if (conflictBookingList.size() == 0) {
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

			// committing transaction
			session.getTransaction().commit();

			return true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			return false;
		}
	}

	/**
	 * To cancel all the remaining pending bookings for the day
	 * 
	 * @return - True if the operation is successful else false
	 */
	@SuppressWarnings("unchecked")
	public boolean cancelTodayBookings() {
		// getting session
		Session session = sessionFactory.openSession();

		try {
			// start transaction
			session.beginTransaction();

			Criteria criteria = session.createCriteria(BookingsModel.class);

			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();

			// Getting the day's pending requests
			criteria.add(Restrictions.and(Restrictions.eq("date", date),
					Restrictions.eq("status", "Pending")));

			List<BookingsModel> listToUpdate = criteria.list();

			// Setting the status of all the pending bookings to cancelled
			for (BookingsModel bookingsModel : listToUpdate) {
				bookingsModel.setStatus("Cancelled");
				session.update(bookingsModel);
			}

			// committing transaction
			session.getTransaction().commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}
	}

}