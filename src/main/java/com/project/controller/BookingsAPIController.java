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
	 * Following function returns the list of all pending bookings corresponding to particular resource.
	 * @param ResourcesVO(ResourcesVO) containing the information of the resource.
	 * @return Response object showing the list of all pending bookings.
	 * @author Vivek Mittal, Pratap Singh
	 */
	@RequestMapping(value = "/bookings/getPendingbookings", method = RequestMethod.POST)
	public @ResponseBody Response getPendingBookingsList(
			@RequestBody ResourcesVO resourcesVO) {

		// Getting the result from the Service Layer
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
	 * Following function updates the status of bookings(rejected/cancelled)
	 * @param bookingsVO(BookingsVO) - contains the information related to the booking
	 * @return Response object confirming the updation
	 * @author Vivek Mittal, Pratap Singh
	 */
	@RequestMapping(value = "/bookings/updateBookingsStatus", method = RequestMethod.POST)
	public @ResponseBody Response updateBookingsStatus(
			@RequestBody BookingsVO bookingsVO) {
		// Getting the result from the Service Layer

		boolean result = bookingsService.updateBookingsStatus(bookingsVO);

		// Sending back the response to the client
		if (result) {
			// Sending the booking updation mail
			String mailMessage = "<p>Dear " + bookingsVO.getUserDetails().getName() + "</p><p>Your booking with ID : "
					+ bookingsVO.getBookingId()
					+ "</p><p>The current booking status is : "
					+ bookingsVO.getStatus()
					+ "</p><br/><p>Regards</p><p>Resource Booking Team</p>";
			mailService.sendHTMLMail(bookingsVO.getUserDetails(),
					"Booking Status Changed", mailMessage);
			
			messageService.sendSMS(mailMessage, "91"+bookingsVO.getUserDetails().getMobileNumber());

			return new Response(200, true);
		} else {
			return new Response(400, "Couldn't update");
		}
	}
	
	/**
	 * To update the booking status to approve for a pending booking
	 * @param bookingsVO - The BookingsVO containing information about the booking to be approveed
	 * @return - Response object confirming the change of status for the booking
	 * @author Arpit Pittie
	 */
	@RequestMapping(value = "/bookings/updateBookingsStatusApproved", method = RequestMethod.POST)
	public @ResponseBody Response updateBookingsStatusApproved(
			@RequestBody BookingsVO bookingsVO) {
		// Getting the result from the facade

		boolean result = bookingsService.updateBookingsStatusApproved(bookingsVO);

		// Sending back the response to the client
		if (result) {
			// Sending the booking updation mail
			String mailMessage = "<p>Dear " + bookingsVO.getUserDetails().getName() + "</p><p>Your booking with ID : "
					+ bookingsVO.getBookingId()
					+ "</p><p>The current booking status is : "
					+ bookingsVO.getStatus()
					+ "</p><br/><p>Regards</p><p>Resource Booking Team</p>";
			mailService.sendHTMLMail(bookingsVO.getUserDetails(),
					"Booking Status Changed", mailMessage);
			
			messageService.sendSMS(mailMessage, "91"+bookingsVO.getUserDetails().getMobileNumber());

			return new Response(200, true);
		} else {
			return new Response(400, "Couldn't update");
		}
	}

	/**
	 * To create a new booking.
	 * @param bookingsVO(BookingsVO) - The view object containing the bookings details
	 * @return Response object containing the booking confirmation
	 * @author Vivek Mittal, Pratap Singh
	 */
	@RequestMapping(value = "/bookings/createBooking", method = RequestMethod.POST)
	public @ResponseBody Response createBooking(
			@RequestBody BookingsVO bookingsVO) {

		// Getting the result from theService Layer
		bookingsVO = bookingsService.createBooking(bookingsVO);

		// Sending back the response to the client
		if (bookingsVO != null) {
			// Sending the mail for the booking creation
			String mailMessage = "<p>Dear "+bookingsVO.getUserDetails().getName()+"</p><p>Your booking for resource : "
					+ bookingsVO.getResourceDetails().getResourceName()
					+ "</p><p> is registered with us with booking ID : "
					+ bookingsVO.getBookingId()
					+ "</p><p>The current booking status is : "
					+ bookingsVO.getStatus()
					+ "</p><p>Regards</p><p>Resource Booking Team</p>";
			mailService.sendHTMLMail(bookingsVO.getUserDetails(),
					"Booking Confirmation", mailMessage);
			
			String message="Dear "+bookingsVO.getUserDetails().getName()+"\n Your booking with ID "+ bookingsVO.getBookingId() + 
					"is : " + bookingsVO.getStatus() + 
					"\n\nRegards\nResource Booking Team";
		
			messageService.sendSMS(message,"91"+bookingsVO.getUserDetails().getMobileNumber());

			return new Response(200, bookingsVO);
		} else {
			return new Response(400, "Error in booking creation");
		}
	}

	/**
	 * To update the booking status for the user approved by the another user via Email.
	 * @param bookingId - Previous Booking Id(Approved)
	 * @param newBookingId - New booking Id(Pending)
	 * @param status - Status(Approved)
	 * @return Response object containing information regarding status updation
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
	 * To get the list of pending bookings for a particular user
	 * @param usersVO - The UsersVO object containing the information about the user
	 * @return - Response object containing the list of pending bookings for the user
	 * @author Amit Sharma
	 */
	@RequestMapping(value = "/bookings/getPendingbookingsByEmployeeId", method = RequestMethod.POST)
	public @ResponseBody Response getPendingBookingsListByEmployeeId(
			@RequestBody UsersVO usersVO) {
		// Getting the result from the Service Layer
		List<BookingsVO> result = bookingsService
				.pendingBookingsListByEmployeeId(usersVO);

		// Sending back the response to the client
		if (result != null) {
			return new Response(200, result);
		} else {
			return new Response(400, "No Pending bookings");
		}
	}
	
	/**
	 * To get the list of approved bookings for a particular user
	 * @param usersVO - The UsersVO object containing the information about the user
	 * @return - Response object containing the list of approved bookings for the user
	 * @author Arpit Pittie
	 */
	@RequestMapping(value = "/bookings/getApprovedbookingsByEmployeeId", method = RequestMethod.POST)
	public @ResponseBody Response getApprovedBookingsListByEmployeeId(
			@RequestBody UsersVO usersVO) {
		// Getting the result from the Service Layer
		List<BookingsVO> result = bookingsService
				.approvedBookingsListByEmployeeId(usersVO);

		// Sending back the response to the client
		if (result != null) {
			return new Response(200, result);
		} else {
			return new Response(400, "No Pending bookings");
		}
	}

	/**
	 * To edit the existing booking.
	 * @param bookingsVO(BookingsVO) containing information about the new edited booking.
 	 * @return Response Object containing information regarding editing of booking.
	 * @author Vivek Mittal, Pratap Singh
	 */
	@RequestMapping(value = "/bookings/editBooking", method = RequestMethod.POST)
	public @ResponseBody Response editBooking(@RequestBody BookingsVO bookingsVO) {

		// Getting the result from Service Layer
		BookingsVO result = bookingsService.editBooking(bookingsVO);
		// Sending back the response to the client
		if (result!=null) {
			return new Response(200, result);
		} else {
			return new Response(400, "Couldn't create");
		}
	}

	/**
	 * To cancel the pending bookings for today's date
	 * @return - Response object confirming the operation success
	 * @author Arpit Pittie
	 */
	@RequestMapping(value = "/bookings/cancelTodayBookings")
	public @ResponseBody Response cancelTodayBookings() {
		if (bookingsService.cancelTodayBookings()) {
			return new Response(200, "OK");
		} else {
			return new Response(405, "Bad Request");
		}
	}

}