package com.project.util;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.project.model.BookingsModel;
import com.project.model.ResourcesModel;
import com.project.model.UsersModel;
import com.project.vo.BookingsVO;
import com.project.vo.ResourcesVO;
import com.project.vo.UsersVO;

@Component
public class Converters {

	
	
	// To get the beans
		@Autowired
		private ApplicationContext context;
	/**
	 * Following function converts the 'BookingVO' object to 'BookingModel'
	 * object
	 * 
	 * @param bookingsVO
	 *            - the 'BookingVO' object to be converted
	 * @return - the converted 'BookingModel' object
	 * @author Vivek Mittal, Pratap Singh
	 */
	public BookingsModel bookingsVoToModel(BookingsVO bookingsVO) {
		BookingsModel bookingsModel = context.getBean(BookingsModel.class);
		UsersModel usersModel = context.getBean(UsersModel.class);
		ResourcesModel resourcesModel = context.getBean(ResourcesModel.class);

		// Copying all the properties based upon their data types
		bookingsModel.setBookingId(bookingsVO.getBookingId());

		bookingsModel.setTitle(bookingsVO.getTitle());
		bookingsModel.setDescription(bookingsVO.getDescription());

		bookingsModel.setNumberOfParticipants(bookingsVO
				.getNumberOfParticipants());

		BeanUtils.copyProperties(bookingsVO.getUserDetails(), usersModel);
		BeanUtils.copyProperties(bookingsVO.getResourceDetails(),
				resourcesModel);

		bookingsModel.setUserDetails(usersModel);
		bookingsModel.setResourceDetails(resourcesModel);

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			bookingsModel.setDate(new Date(dateFormat.parse(
					bookingsVO.getDate()).getTime()));

			dateFormat = new SimpleDateFormat("HH:mm:ss");
			bookingsModel.setStartTime(new Time(dateFormat.parse(
					bookingsVO.getStartTime()).getTime()));
			bookingsModel.setEndTime(new Time(dateFormat.parse(
					bookingsVO.getEndTime()).getTime()));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return bookingsModel;
	}

	/**
	 * To convert BookingsModel to BookingsVO
	 * 
	 * @param bookingsModel
	 *            - the 'BookingModel' object to be converted
	 * @return - the converted 'BookingVO' object
	 * @author Vivek Mittal, Pratap Singh
	 */
	public BookingsVO bookingsModelToBookingsVO(BookingsModel bookingsModel) {
		System.out.println(context+"modeltovocontext");
		ResourcesModel resourcesModel = context.getBean(ResourcesModel.class);
		UsersModel usersModel = context.getBean(UsersModel.class);
		UsersVO usersVO = context.getBean(UsersVO.class);

		BookingsVO bookingsVO = context.getBean(BookingsVO.class);

		// Copying all the properties based upon their data types
		bookingsVO.setBookingId(bookingsModel.getBookingId()); // 1

		resourcesModel = bookingsModel.getResourceDetails();

		ResourcesVO resourcesVO = context.getBean(ResourcesVO.class);
		BeanUtils.copyProperties(resourcesModel, resourcesVO);
		bookingsVO.setResourceDetails(resourcesVO); // 2

		// Getting the current date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String date = dateFormat.format(bookingsModel.getDate());
		bookingsVO.setDate(date); // 3

		dateFormat = new SimpleDateFormat("HH:mm");

		date = dateFormat.format(bookingsModel.getStartTime());
		bookingsVO.setStartTime(date); // 4

		date = dateFormat.format(bookingsModel.getEndTime());
		bookingsVO.setEndTime(date); // 5

		bookingsVO.setNumberOfParticipants(bookingsModel
				.getNumberOfParticipants()); // 6

		usersModel = bookingsModel.getUserDetails();
		BeanUtils.copyProperties(usersModel, usersVO);
		bookingsVO.setUserDetails(usersVO); // 7

		bookingsVO.setStatus(bookingsModel.getStatus()); // 8

		bookingsVO.setTitle(bookingsModel.getTitle());
		bookingsVO.setDescription(bookingsModel.getDescription());

		return bookingsVO;
	}
}
