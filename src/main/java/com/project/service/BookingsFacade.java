
package com.project.service;

/**
 * Class to provide a single API for the database interaction with the controllers
* @author Pratap Singh Ranawat and Vivek Mittal
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.project.model.BookingsVO;
import com.project.model.ResourcesVO;
@Service("bookingsFacade")
public class BookingsFacade {

	@Autowired
	private BookingsService bookingsService;
	
	@Autowired
	private ApplicationContext context;	//To get the beans
	
	
	/**
	 * To get the list of pending bookings corresponding to particular resource ID
	 * @return List of bookings having status = pending
	 */
	public List<BookingsVO> pendingBookingsListById(ResourcesVO resourcesVO) {
		return bookingsService.pendingBookingsListById(resourcesVO);
	}
	/**
	 * Following function fetches the list of all approved bookings
	 * @return List of bookings having status = Approved
	 */
	public List<BookingsVO> approvedBookingsList() {
		List<BookingsVO> list = bookingsService.approvedBookingsList();
		System.out.println(list.size()+"Reply from service layer");
		return list;
	}
	
	/**
	 * Following function updates the status of bookings(accepted/cancelled)
	 * @param bookingsVO contains the information related to the booking
	 * @return true/false whether booking status has been updated successfully.
	 */
	public boolean updateBookingsStatus(BookingsVO bookingsVO){
		
		boolean result = bookingsService.updateBookingsStatus(bookingsVO);
		return result;
	}
	
}