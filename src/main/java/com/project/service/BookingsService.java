package com.project.service; 

/**
 * Class To implement the service layer for the Users
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

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

import com.project.model.BookingsModel;
import com.project.model.BookingsVO;
import com.project.model.ResourcesModel;
import com.project.model.ResourcesVO;
import com.project.model.UsersModel;
import com.project.model.UsersVO;

@Service("bookingsService")
@Transactional
public class BookingsService {

	@Autowired
	private BookingsDAO bookingsDAO;
	
	//To get the beans
	@Autowired
	private ApplicationContext context;
	
	/**
	 * To get the list of pending bookings using resource id from BookingsDAO class
	 * @return List of bookings having status = pending
	 */
	public List<BookingsVO> pendingBookingsListById(ResourcesVO resourcesVO) {
		
		BookingsModel bookingsModel = context.getBean(BookingsModel.class);
		ResourcesModel resourcesModel = context.getBean(ResourcesModel.class);
		List<BookingsModel> bookingsList;
		List<BookingsVO> bookingsVOList = new ArrayList<BookingsVO>();
		
		
		BeanUtils.copyProperties(resourcesVO, resourcesModel);
		

		bookingsModel.setResourceDetails(resourcesModel);
		
		
		try {
			// Getting the result from the database
			bookingsList =  bookingsDAO.pendingBookingsListById(bookingsModel);
			
			for (BookingsModel bookingsModelLocal : bookingsList) {
				
				bookingsVOList.add(convertBookingsModelToBookingsVO(bookingsModelLocal));
			
			}
			
		
			return bookingsVOList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * To get the list of pending bookings using employee ID from BookingsDAO class
	 * @return List of bookings having status = pending
	 */
    public List<BookingsVO> pendingBookingsListByEmployeeId(UsersVO usersVO) {
		
		UsersModel usersModel = context.getBean(UsersModel.class);
		List<BookingsModel> bookingsList;
		List<BookingsVO> bookingsVOList = new ArrayList<BookingsVO>();
		
		
		BeanUtils.copyProperties(usersVO,usersModel );
		
		try {
			// Getting the result from the database
			bookingsList =  bookingsDAO.pendingBookingsListByEmployeeId(usersModel);
			
			for (BookingsModel bookingsModelLocal : bookingsList) {
				
				bookingsVOList.add(convertBookingsModelToBookingsVO(bookingsModelLocal));
			
			}
			
		
			return bookingsVOList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * To get the list of approved bookings from BookingsDAO class
	 * @return List of bookings having status = Approved
	 */
	public List<BookingsVO> approvedBookingsList() {
		
		List<BookingsModel> bookingsList;
		List<BookingsVO> bookingsVOList = new ArrayList<BookingsVO>();
		
		try {
			// Getting the result from the database
			bookingsList =  bookingsDAO.approvedBookingsList();
			
			for (BookingsModel bookingsModelLocal : bookingsList) {
				bookingsVOList.add(convertBookingsModelToBookingsVO(bookingsModelLocal));
			}
			return bookingsVOList;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<BookingsVO>();
		}
	}

	/**
	 * Following function updates the status of bookings(accepted/cancelled)
	 * @param bookingsVO contains the information related to the booking
	 * @return true/false whether booking status has been updated successfully.
	 */
	public boolean updateBookingsStatus(BookingsVO bookingsVO){
		
		BookingsModel bookingsModel = context.getBean(BookingsModel.class);
		//copying the bookingsVO data to the bookingsModel
		BeanUtils.copyProperties(bookingsVO, bookingsModel);
		//getting the result from the database
		
		return bookingsDAO.updateBookingsStatus(bookingsModel);
	}
	
	public BookingsVO createBooking(BookingsVO bookingsVO) {
		BookingsModel bookingsModel;
				
		bookingsModel = BookingsVoToModel(bookingsVO);
		bookingsModel = bookingsDAO.createBooking(bookingsModel);
		
		if(bookingsModel != null) {
			bookingsVO = convertBookingsModelToBookingsVO(bookingsModel);
			return bookingsVO;
		} else {
			return null;
		}
		
		//getting the result from the database
	}
	
	public BookingsModel BookingsVoToModel(BookingsVO bookingsVO) {
		BookingsModel bookingsModel = context.getBean(BookingsModel.class);
		UsersModel usersModel = context.getBean(UsersModel.class);
		ResourcesModel resourcesModel = context.getBean(ResourcesModel.class);
		
		bookingsModel.setTitle(bookingsVO.getTitle());
		bookingsModel.setDescription(bookingsVO.getDescription());
		
		bookingsModel.setNumberOfParticipants(bookingsVO.getNumberOfParticipants());
		
		BeanUtils.copyProperties(bookingsVO.getUserDetails(), usersModel);
		BeanUtils.copyProperties(bookingsVO.getResourceDetails(), resourcesModel);
		
		bookingsModel.setUserDetails(usersModel);
		bookingsModel.setResourceDetails(resourcesModel);
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			bookingsModel.setDate(new Date(dateFormat.parse(bookingsVO.getDate()).getTime()));
			
			dateFormat = new SimpleDateFormat("HH:mm:ss");
			bookingsModel.setStartTime(new Time(dateFormat.parse(bookingsVO.getStartTime()).getTime()));
			bookingsModel.setEndTime(new Time(dateFormat.parse(bookingsVO.getEndTime()).getTime()));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return bookingsModel;
	}
	
	/**
	 * To convert BookingsModel to BookingsVO
	 * @return BookingsVO
	 */
	private BookingsVO convertBookingsModelToBookingsVO(BookingsModel bookingsModel){
		
		
		ResourcesModel resourcesModel = context.getBean(ResourcesModel.class);
		UsersModel usersModel = context.getBean(UsersModel.class);
		UsersVO usersVO = context.getBean(UsersVO.class);

		
		BookingsVO bookingsVO = context.getBean(BookingsVO.class);
		
		bookingsVO.setBookingId(bookingsModel.getBookingId());			//1
		
		resourcesModel = bookingsModel.getResourceDetails();
		
		ResourcesVO resourcesVO = context.getBean(ResourcesVO.class);
		BeanUtils.copyProperties(resourcesModel, resourcesVO );
		System.out.println(resourcesVO.hashCode());
		bookingsVO.setResourceDetails(resourcesVO);						//2
		
		// Getting the current date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		String date = dateFormat.format(bookingsModel.getDate());
		bookingsVO.setDate(date);										//3
		
		dateFormat = new SimpleDateFormat("HH:mm");
		
		date = dateFormat.format(bookingsModel.getStartTime());
		bookingsVO.setStartTime(date);									//4
		
		date = dateFormat.format(bookingsModel.getEndTime());
		bookingsVO.setEndTime(date);									//5
		
		bookingsVO.setNumberOfParticipants(bookingsModel.getNumberOfParticipants());	//6
		
		usersModel = bookingsModel.getUserDetails();
		BeanUtils.copyProperties(usersModel, usersVO);
		bookingsVO.setUserDetails(usersVO);								//7
		
		bookingsVO.setStatus(bookingsModel.getStatus());				//8
		
		bookingsVO.setTitle(bookingsModel.getTitle());                 //9
		bookingsVO.setDescription(bookingsModel.getDescription());     //10
		
		System.out.println(bookingsVO);
		return bookingsVO;
	}
	
	/*  edit booking */
	public boolean editBooking(BookingsVO bookingsVO) {
				BookingsModel bookingsModel;
	
		 		bookingsModel = BookingsVoToModel(bookingsVO);
		 		return bookingsDAO.editBooking(bookingsModel);
		 	}
}