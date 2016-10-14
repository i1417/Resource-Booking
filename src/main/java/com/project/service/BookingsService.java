package com.project.service; 

/**
 * Class To implement the service layer for the Users
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

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
	 * To get the list of pending bookings
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

			// Checking if the user with the given credentials exist or not
			if (bookingsVOList.size() == 0) {
				return null;
			} else {
				// Copying the properties from Model to VO Object
				return bookingsVOList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
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
		System.out.println(bookingsVO);
		return bookingsVO;
	}
	
	
	/**
	 * To get the list of approved bookings for specific Resource
	 * @return List of bookings having status = Approved
	 */
	public List<BookingsVO> approvedBookingsList() {
		
		List<BookingsModel> bookingsList;
		List<BookingsVO> bookingsVOList = new ArrayList<BookingsVO>();
		
		try {
			// Getting the result from the database
			bookingsList =  bookingsDAO.approvedBookingsList();
			System.out.println(bookingsList.size()+"Response from dao");
			System.out.println(bookingsList);
			for (BookingsModel bookingsModelLocal : bookingsList) {
				System.out.println("In the loop");
				bookingsVOList.add(convertBookingsModelToBookingsVO(bookingsModelLocal));
			
			}

			// Checking if the user with the given credentials exist or not
			if (bookingsVOList.size() == 0) {
				return null;
			} else {
				// Copying the properties from Model to VO Object
				return bookingsVOList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
