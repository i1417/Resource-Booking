
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
	 * To get the list of pending bookings
	 * @return List of bookings having status = pending
	 */
	public List<BookingsVO> pendingBookingsListById(ResourcesVO resourcesVO) {
		return bookingsService.pendingBookingsListById(resourcesVO);
	}
	/**
	 * To get the list of pending bookings
	 * @return List of bookings having status = pending
	 */
	public List<BookingsVO> approvedBookingsList() {
		List f = bookingsService.approvedBookingsList();
		System.out.println(f.size()+"Reply from service layer");
		return f;
	}
	
}
