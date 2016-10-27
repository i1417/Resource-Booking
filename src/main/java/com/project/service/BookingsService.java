/**
 * Class To implement the service layer for the Users
 * @author Pratap Singh Ranawat and Vivek Mittal
 */
package com.project.service;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.project.dao.BookingsDAO;
import com.project.model.BookingsModel;
import com.project.model.ResourcesModel;
import com.project.model.UsersModel;
import com.project.vo.BookingsVO;
import com.project.vo.ResourcesVO;
import com.project.vo.UsersVO;

@Service("bookingsService")
@Transactional
public class BookingsService {

	// To interact with the DAO Layer
	@Autowired
	private BookingsDAO bookingsDAO;

	// To get the beans
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private Converters converter;

	/**
	 * To get the list of pending bookings using resource id
	 * @return List of bookings having status = pending
	 * @author Vivek Mittal, Pratap Singh
	 */
	public List<BookingsVO> pendingBookingsListById(ResourcesVO resourcesVO) {

		BookingsModel bookingsModel = context.getBean(BookingsModel.class);
		ResourcesModel resourcesModel = context.getBean(ResourcesModel.class);
		
		List<BookingsModel> bookingsList;
		List<BookingsVO> bookingsVOList = new ArrayList<BookingsVO>();

		//Converting resourcesVO to resourcesModel
		BeanUtils.copyProperties(resourcesVO, resourcesModel);

		bookingsModel.setResourceDetails(resourcesModel);

		try {
			// Getting the result from the database
			bookingsList = bookingsDAO.pendingBookingsListById(bookingsModel);

			//copying fetched pending booking list in some local bookingsModel
			for (BookingsModel bookingsModelLocal : bookingsList) {

				//adding bookingsModelLocal data in bookingsVOList one by one  
				bookingsVOList
						.add(converter.bookingsModelToBookingsVO(bookingsModelLocal));

			}

			return bookingsVOList;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<BookingsVO>();
		}

	}

	/**
	 * To get the list of pending bookings using employee ID
	 * @param usersVO(UsersVO) contains user details (employee Id)
	 * @return BookingVO List of bookings having status = pending
	 * @author Amit Sharma
	 */
	public List<BookingsVO> pendingBookingsListByEmployeeId(UsersVO usersVO) {

		UsersModel usersModel = context.getBean(UsersModel.class);
		List<BookingsModel> bookingsList;
		List<BookingsVO> bookingsVOList = new ArrayList<BookingsVO>();

		BeanUtils.copyProperties(usersVO, usersModel);

		try {
			// Getting the result from the bookingsDAO
			bookingsList = bookingsDAO
					.pendingBookingsListByEmployeeId(usersModel);

			//Converting UserModel to UserVO for sending data back to controller
			for (BookingsModel bookingsModelLocal : bookingsList) {

				bookingsVOList
						.add(converter.bookingsModelToBookingsVO(bookingsModelLocal));

			}

			// returns List of BookingsVO
			return bookingsVOList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * To get all the approved bookings list for the particular user
	 * @param usersVO - The user details to fetch the bookings
	 * @return - The BookingsVO list containing the approved bookings
	 * @author Arpit Pittie
	 */
	public List<BookingsVO> approvedBookingsListByEmployeeId(UsersVO usersVO) {

		UsersModel usersModel = context.getBean(UsersModel.class);
		List<BookingsModel> bookingsList;
		List<BookingsVO> bookingsVOList = new ArrayList<BookingsVO>();

		BeanUtils.copyProperties(usersVO, usersModel);

		try {
			// Getting the result from the bookingsDAO
			bookingsList = bookingsDAO
					.approvedBookingsListByEmployeeId(usersModel);

			//Converting UserModel to UserVO for sending data back to controller
			for (BookingsModel bookingsModelLocal : bookingsList) {

				bookingsVOList
						.add(converter.bookingsModelToBookingsVO(bookingsModelLocal));

			}

			// returns List of BookingsVO
			return bookingsVOList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * To get the list of approved bookings from BookingsDAO class
	 * @return List of bookings having status = Approved
	 * @author Vivek Mittal, Pratap Singh
	 */
	public List<BookingsVO> approvedBookingsList(int employeeId) {

		List<BookingsModel> bookingsList;
		List<BookingsVO> bookingsVOList = new ArrayList<BookingsVO>();
		
		UsersModel userModel = context.getBean(UsersModel.class);
		userModel.setEmployeeId(employeeId);

		try {
			// Getting the result from the database
			bookingsList = bookingsDAO.approvedBookingsListOtherUsers(userModel);

			//Converting BookingsModel to BookingsVO for sending data back to controller
			for (BookingsModel bookingsModelLocal : bookingsList) {
				bookingsVOList
						.add(converter.bookingsModelToBookingsVO(bookingsModelLocal));
			}

			// returns List ofBookingsVO
			return bookingsVOList;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<BookingsVO>();
		}
	}

	/**
	 * Following function updates the status of bookings(rejected/cancelled)
	 * 
	 * @param bookingsVO
	 *            contains the information related to the booking
	 * @return true/false whether booking status has been updated successfully.
	 * @author Vivek Mittal, Pratap Singh
	 */
	public boolean updateBookingsStatus(BookingsVO bookingsVO) {

		BookingsModel bookingsModel = context.getBean(BookingsModel.class);
		// copying the bookingsVO data to the bookingsModel
		BeanUtils.copyProperties(bookingsVO, bookingsModel);
		// getting the result from the database

		return bookingsDAO.updateBookingsStatus(bookingsModel);
	}
	
	/**
	 * Following method updates the booking status to approve for pending booking
	 * @param bookingsVO - The booking details whose status to be changed to approve
	 * @return - True if the status is changed successfully else false
	 * @author Arpit Pittie
	 */
	public boolean updateBookingsStatusApproved(BookingsVO bookingsVO) {

		BookingsModel bookingsModel = context.getBean(BookingsModel.class);
		// copying the bookingsVO data to the bookingsModel
		bookingsModel = converter.bookingsVoToModel(bookingsVO);
		bookingsModel.setStatus(bookingsVO.getStatus());
		// getting the result from the database

		return bookingsDAO.updateBookingsStatusApproved(bookingsModel);
	}

	/**
	 *  this function is helper service to get BookingsVO and convert it to BookingsModel
	 *  and call createBookings to create new Booking
	 * @param bookingsVO - contains details for new booking
	 * @return BookingsVO - The BookingsVO containing all the information regarding the new booking
	 * @author Vivek Mittal, Pratap Singh
	 */
	public BookingsVO createBooking(BookingsVO bookingsVO) {
		BookingsModel bookingsModel;

		// converts BookingsVO to BookingsModel
		bookingsModel = converter.bookingsVoToModel(bookingsVO);
		bookingsModel = bookingsDAO.createBooking(bookingsModel);

		if (bookingsModel != null) {
			//converts back to BookingsVO from BookingsModels
			bookingsVO = converter.bookingsModelToBookingsVO(bookingsModel);
			return bookingsVO;
		} else {
			return null;
		}

		// getting the result from the database
	}

	/**
	 * Following function edits the existing booking
	 * @param bookingsVO contains the information of the edited booking.
	 * @return BookingsVO - The BookingsVO containing all the information regarding the edited booking
	 * @author Vivek Mittal, Pratap Singh
	 */
	public BookingsVO editBooking(BookingsVO bookingsVO) {
		BookingsModel bookingsModel;
		
		//Copying the properties of boookingsVO to bookingsModel
		bookingsModel = converter.bookingsVoToModel(bookingsVO);
		return  converter.bookingsModelToBookingsVO(bookingsDAO.editBooking(bookingsModel));
	}

	/**
	 * To change the status of user booking to cancelled in the 
	 * view of other user's booking to approve
	 * @param bookingId - The booking to be cancelled
	 * @param newBookingId - The booking to be approved
	 * @param status - The status of the new booking to set
	 * @return - True if the operation is successful else false
	 * @author Arpit Pittie
	 */
	public boolean userBookingStatusChange(String bookingId,
			String newBookingId, String status) {
		return bookingsDAO.userBookingStatusChange(bookingId, newBookingId,
				status);
	}
	
	/**
	 * To cancel all the today's pending bookings
	 * @return - True if the operation is successful else false
	 * @author Arpit Pittie
	 */
	public boolean cancelTodayBookings() {
		return bookingsDAO.cancelTodayBookings();
	}
}
