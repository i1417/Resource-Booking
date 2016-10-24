package com.project.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.project.model.BookingsVO;
import com.project.model.ResourcesVO;
import com.project.model.UsersVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml" })
@WebAppConfiguration
public class TestBookingsAPIController {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private BookingsVO booking;
	private ResourcesVO resource;
	private UsersVO user;
	private ObjectMapper mapper;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(
				this.webApplicationContext).build();
		booking = new BookingsVO();
		resource = new ResourcesVO();
		user = new UsersVO();
		mapper = new ObjectMapper();
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testGetPendingBookingsList() throws Exception {
		resource.setResourceId(100);

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(resource);

		this.mockMvc.perform(
				post("/bookings/getPendingbookings").contentType(
						MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testGetApprovedBookingsList() throws Exception {
		this.mockMvc.perform(
				get("/bookings/getApprovedBookings?employeeId=10011").accept(
						MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testUpdateBookingsStatus() throws Exception {
		resource.setResourceId(100);

		user.setEmployeeId(10011);
		user.setEmail("anant.sharma@company.com");

		booking.setBookingId("B3F2C220161021-1");
		booking.setDate("2016-10-26");
		booking.setDescription("Meeting with team");
		booking.setNumberOfParticipants(10);
		booking.setTitle("Scrum Meeting");
		booking.setStartTime("11:30:00");
		booking.setEndTime("12:15:00");
		booking.setResourceDetails(resource);
		booking.setUserDetails(user);
		booking.setStatus("Rejected");

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(booking);

		this.mockMvc.perform(
				post("/bookings/updateBookingsStatus").contentType(
						MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testUpdateBookingsStatusApproved() throws Exception {
		booking.setBookingId("B3F2C220161021-1");
		booking.setStatus("Approved");

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(booking);

		this.mockMvc.perform(
				post("/bookings/updateBookingsStatusApproved").contentType(
						MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testCreateBooking() throws Exception {
		resource.setResourceId(100);

		user.setEmployeeId(10011);
		user.setEmail("anant.sharma@company.com");

		booking.setDate("2016-10-26");
		booking.setDescription("Meeting with team");
		booking.setNumberOfParticipants(10);
		booking.setTitle("Scrum Meeting");
		booking.setStartTime("11:30:00");
		booking.setEndTime("12:15:00");
		booking.setResourceDetails(resource);
		booking.setUserDetails(user);

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(booking);

		this.mockMvc.perform(
				post("/bookings/updateBookingsStatus").contentType(
						MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testUserBookingStatusChange() throws Exception {
		this.mockMvc
				.perform(
						get(
								"/bookings/statusChange?bookingId=B3F2C220161021-1&newBookingId=B3F2C220161026-1&status=Approved")
								.accept(MediaType.APPLICATION_JSON)).andExpect(
						status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testGetPendingBookingsListByEmployeeId() throws Exception {
		user.setEmployeeId(10011);

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(booking);

		this.mockMvc.perform(
				post("/bookings/getPendingbookingsByEmployeeId").contentType(
						MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testGetApprovedBookingsListByEmployeeId() throws Exception {
		user.setEmployeeId(10011);

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(booking);

		this.mockMvc.perform(
				post("/bookings/getApprovedbookingsByEmployeeId").contentType(
						MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testEditBooking() throws Exception {
		resource.setResourceId(100);

		user.setEmployeeId(10011);

		booking.setBookingId("B3F2C220161026-1");
		booking.setDate("2016-10-26");
		booking.setDescription("Meeting with team");
		booking.setNumberOfParticipants(10);
		booking.setTitle("Scrum Meeting");
		booking.setStartTime("11:30:00");
		booking.setEndTime("12:15:00");
		booking.setResourceDetails(resource);
		booking.setUserDetails(user);

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(booking);

		this.mockMvc.perform(
				post("/bookings/editBooking").contentType(
						MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testCancelTodayBookings() throws Exception {
		this.mockMvc.perform(
				get("/bookings/cancelTodayBookings").accept(
						MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

}
