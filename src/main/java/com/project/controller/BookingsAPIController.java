/**
 * Class to manage the Resource API 
 * @author Pratap Singh Ranawat, Vivek Mittal
 * @author Arpit Pittie, Amit Sharma
 */
package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.model.BookingsVO;
import com.project.model.ResourcesVO;
import com.project.model.UsersVO;
import com.project.service.BookingsService;
import com.project.service.MailService;
import com.project.service.MessageService;

@Controller
public class BookingsAPIController {

	// To interact with the service layer
	@Autowired
	private BookingsService bookingsService;

	@Autowired
	private MailService mailService;
	
	@Autowired
	private MessageService messageService;

	// To get the beans
	@Autowired
	private ApplicationContext context;

	/**
	 * Following function returns the list of all pending bookings.
	 * 
	 * @return Response object showing the list of all pending bookings.
	 * @author Vivek Mittal, Pratap Singh
	 */
	@RequestMapping(value = "/bookings/getPendingbookings", method = RequestMethod.POST)
	public @ResponseBody Response getPendingBookingsList(
			@RequestBody ResourcesVO resourcesVO) {

		// Getting the result from the facade
		List<BookingsVO> result = bookingsService
				.pendingBookingsListById(resourcesVO);

		// Sending back the response to the client
		if (result != null) {
			return new Response(200, result);
		} else {
			return new Response(400, "No Pending bookings");
		}
	}

	/**
	 * Following function returns the list of all bookings which are approved
	 * till today and upcoming.
	 * 
	 * @return Response object showing the list of all approved bookings.
	 * @author Vivek Mittal, Pratap Singh
	 */
	@RequestMapping(value = "/bookings/getApprovedBookings", method = RequestMethod.GET)
	public @ResponseBody Response getApprovedBookingsList(@RequestParam("employeeId") String employeeId) {
		// Getting the result from the facade
		List<BookingsVO> result = bookingsService.approvedBookingsList(Integer.parseInt(employeeId));

		// Sending back the response to the client
		if (result.size() != 0) {
			return new Response(200, result);
		} else {
			return new Response(400, "No new bookings");
		}
	}

	/**
	 * Following function updates the status of bookings(accepted/cancelled)
	 * 
	 * @param bookingsVO
	 *            - contains the information related to the booking
	 * 
	 * @return Response object confirming the update
	 * @author Vivek Mittal, Pratap Singh
	 */
	@RequestMapping(value = "/bookings/updateBookingsStatus", method = RequestMethod.POST)
	public @ResponseBody Response updateBookingsStatus(
			@RequestBody BookingsVO bookingsVO) {
		// Getting the result from the facade

		boolean result = bookingsService.updateBookingsStatus(bookingsVO);

		// Sending back the response to the client
		if (result) {
			// Sending the booking updation mail
			String mailMessage = "Your booking with ID : "
					+ bookingsVO.getBookingId()
					+ "\nThe current booking status is : "
					+ bookingsVO.getStatus()
					+ "\n\nRegards\nResource Booking Team";
			mailService.sendMail(bookingsVO.getUserDetails(),
					"Booking Status Changed", mailMessage);
			
			messageService.sendSMS(mailMessage, "91"+bookingsVO.getUserDetails().getMobileNumber());

			return new Response(200, true);
		} else {
			return new Response(400, "Couldn't update");
		}
	}

	/**
	 * To create a new booking
	 * 
	 * @param bookingsVO
	 *            - The view object containing the bookings details
	 * 
	 * @return Response object containing the booking confirmation
	 * @author Vivek Mittal, Pratap Singh
	 * 
	 */
	@RequestMapping(value = "/bookings/createBooking", method = RequestMethod.POST)
	public @ResponseBody Response createBooking(
			@RequestBody BookingsVO bookingsVO) {

		// Getting the result from the facade
		bookingsVO = bookingsService.createBooking(bookingsVO);

		// Sending back the response to the client
		if (bookingsVO != null) {
			// Sending the mail for the booking creation
			String mailMessage = "Your booking for resource : "
					+ bookingsVO.getResourceDetails().getResourceName()
					+ "\n is registered with us with booking ID : "
					+ bookingsVO.getBookingId()
					+ "\nThe current booking status is : "
					+ bookingsVO.getStatus()
					+ "\n\nRegards\nResource Booking Team";
			mailService.sendMail(bookingsVO.getUserDetails(),
					"Booking Confirmation", mailMessage);
			
			String message="Dear "+bookingsVO.getUserDetails().getName()+"\n Your booking with ID "+ bookingsVO.getBookingId() + 
					"is : " + bookingsVO.getStatus() + 
					"\n\nRegards\nResource Booking Team";
		
			messageService.sendSMS(message,"91"+bookingsVO.getUserDetails().getMobileNumber());

			return new Response(200, true);
		} else {
			return new Response(400, "Error in booking creation");
		}
	}

	/**
	 * 
	 * @param bookingId
	 * @param newBookingId
	 * @param status
	 * @return
	 * @author Arpit Pittie
	 */
	@RequestMapping(value = "/bookings/statusChange", method = RequestMethod.GET)
	public @ResponseBody Response userBookingStatusChange(
			@RequestParam("bookingId") String bookingId,
			@RequestParam("newBookingId") String newBookingId,
			@RequestParam("status") String status) {

		if (bookingsService.userBookingStatusChange(bookingId, newBookingId,
				status)) {
			return new Response(200, "OK");
		} else {
			return new Response(405, "Bad Request");
		}
	}

	/**
	 * 
	 * @param usersVO
	 * @return
	 * @author Amit Sharma
	 */
	@RequestMapping(value = "/bookings/getPendingbookingsByEmployeeId", method = RequestMethod.POST)
	public @ResponseBody Response getPendingBookingsListByEmployeeId(
			@RequestBody UsersVO usersVO) {
		// Getting the result from the facade
		System.out.println("pending requests by employee id:"
				+ usersVO.getEmployeeId());
		List<BookingsVO> result = bookingsService
				.pendingBookingsListByEmployeeId(usersVO);

		// Sending back the response to the client
		if (result != null) {
			System.out.println("OK");
			return new Response(200, result);
		} else {
			System.out.println("Wrong");
			return new Response(400, "No Pending bookings");
		}
	}

	/**
	 * 
	 * @param bookingsVO
	 * @return
	 * @author Vivek Mittal, Pratap Singh
	 */
	@RequestMapping(value = "/bookings/editBooking", method = RequestMethod.POST)
	public @ResponseBody Response editBooking(@RequestBody BookingsVO bookingsVO) {

		System.out.println(bookingsVO);
		// Getting the result from the facade
		boolean result = bookingsService.editBooking(bookingsVO);

		// Sending back the response to the client
		if (result) {
			System.out.println("Edited successfully");
			return new Response(200, result);
		} else {
			System.out.println("Couldn't edit");
			return new Response(400, "Couldn't create");
		}
	}

}