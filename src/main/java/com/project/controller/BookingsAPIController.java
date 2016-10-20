package com.project.controller;

/**
 * Class to manage the Resource API 
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.model.BookingsVO;
import com.project.model.ResourcesVO;
import com.project.service.BookingsFacade;
import com.project.service.MailService;


@Controller
public class BookingsAPIController {
	
	//To interact with the facade layer
	@Autowired
	private BookingsFacade  bookingsFacade;
	
	@Autowired
	private MailService mailService;
	
	//To get the beans
	@Autowired
	private ApplicationContext context;
	
	/**
	 * Following function returns the list of all pending bookings.
	 * @return Response object showing the list of all pending bookings.
	 */
	@RequestMapping(value = "/bookings/getPendingbookings", method = RequestMethod.POST)
	public @ResponseBody Response getPendingBookingsList(@RequestBody ResourcesVO resourcesVO) {
		//Getting the result from the facade
		List<BookingsVO> result = bookingsFacade.pendingBookingsListById(resourcesVO);
		
		
		//Sending back the response to the client
		if(result != null) {
			System.out.println("OK");
			return new Response(200, result);
		} else {
			System.out.println("Wrong");
			return new Response(400, "No Pending bookings");
		}
	}
	
	/**
	 * Following function returns the list of all bookings which are approved till today and upcoming.
	 * @return Response object showing the list of all approved bookings.
	 */
	@RequestMapping(value = "/bookings/getApprovedBookings", method = RequestMethod.GET)
	public @ResponseBody Response getApprovedBookingsList() {
		//Getting the result from the facade
		List<BookingsVO> result = bookingsFacade.approvedBookingsList();
		
		//Sending back the response to the client
		if(result.size() != 0) {
			return new Response(200, result);
		} else {
			return new Response(400, "No new bookings");
		}
	}
	
	/**
	 * Following function updates the status of bookings(accepted/cancelled)
	 * @param bookingsVO contains the information related to the booking
	 * @return 
	 */
	@RequestMapping(value = "/bookings/updateBookingsStatus", method = RequestMethod.POST)
	public @ResponseBody Response updateBookingsStatus(@RequestBody BookingsVO bookingsVO) {
		//Getting the result from the facade
		
		boolean result = bookingsFacade.updateBookingsStatus(bookingsVO);
		
		//Sending back the response to the client
		if(result) {
			String mailMessage = "Your booking with ID : " + bookingsVO.getBookingId() + 
					"\nThe current booking status is : " + bookingsVO.getStatus() + 
					"\n\nRegards\nResource Booking Team";
			
			mailService.sendMail(bookingsVO.getUserDetails(), "Booking Status Changed", mailMessage);
			
			return new Response(200, true);
		} else {
			System.out.println("Couldn't update");
			return new Response(400, "Couldn't update");
		}
	}
	
	/**
	 * To create a new booking
	 * @param bookingsVO - The view object containing the bookings details
	 * @return Booking confirmation
	 */
	@RequestMapping(value = "/bookings/createBooking", method = RequestMethod.POST)
	public @ResponseBody Response createBooking(@RequestBody BookingsVO bookingsVO) {
//		System.out.println(resourceVO);
//		bookingsVO.setResourceDetails(resourceVO);
		System.out.println(bookingsVO);
		
		//Getting the result from the facade
		bookingsVO = bookingsFacade.createBooking(bookingsVO);
//		boolean result = false;
		
		//Sending back the response to the client
		if(bookingsVO != null) {
			String mailMessage = "Your booking for resource : " + bookingsVO.getResourceDetails().getResourceName() +
					"\n is registered with us with booking ID : " + bookingsVO.getBookingId() + 
					"\nThe current booking status is : " + bookingsVO.getStatus() + 
					"\n\nRegards\nResource Booking Team";
			
			mailService.sendMail(bookingsVO.getUserDetails(), "Booking Confirmation", mailMessage);
			System.out.println("Updated successfully");
			return new Response(200, true);
		} else {
			System.out.println("Couldn't update");
			return new Response(400, "Error in booking creation");
		}
	}
	/* to edit existing booking*/
	@RequestMapping(value = "/bookings/editBooking", method = RequestMethod.POST)
	public @ResponseBody Response editBooking(@RequestBody BookingsVO bookingsVO) {

		System.out.println(bookingsVO);
		//Getting the result from the facade
		boolean result = bookingsFacade.editBooking(bookingsVO);
		
		//Sending back the response to the client
		if(result) {
			System.out.println("Edited successfully");
			return new Response(200, result);
		} else {
			System.out.println("Couldn't edit");
			return new Response(400, "Couldn't create");
		}
	}
	
}
