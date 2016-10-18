/**
 * Class to map the Booking View Object
 * @author Arpit Pittie
 */
package com.project.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BookingsVO {

	private String bookingId;
	private UsersVO userDetails;
	private ResourcesVO resourceDetails;
	private String title;
	private String description;
	private String date;
	private String startTime;
	private String endTime;
	private String status;
	private int numberOfParticipants;
	
	public String getBookingId() {
		return bookingId;
	}
	
	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}
	
	@Override
	public String toString() {
		return "BookingsVO [bookingId=" + bookingId + ", userDetails="
				+ userDetails + ", resourceDetails=" + resourceDetails
				+ ", title=" + title + ", description=" + description
				+ ", date=" + date + ", startTime=" + startTime + ", endTime="
				+ endTime + ", status=" + status + ", numberOfParticipants="
				+ numberOfParticipants + "]";
	}

	public UsersVO getUserDetails() {
		return userDetails;
	}
	
	public void setUserDetails(UsersVO userDetails) {
		this.userDetails = userDetails;
	}
	
	public ResourcesVO getResourceDetails() {
		return resourceDetails;
	}
	
	public void setResourceDetails(ResourcesVO resourceDetails) {
		this.resourceDetails = resourceDetails;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getNumberOfParticipants() {
		return numberOfParticipants;
	}
	
	public void setNumberOfParticipants(int numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}
}
