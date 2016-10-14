/**
 * To map the bookings table in the database in the ORM
 * @author Arpit Pittie
 */
package com.project.model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Component
@Table(name = "bookings")
public class BookingsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id")
	private String bookingId;
	
	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private UsersModel userDetails;
	
	@ManyToOne
	@JoinColumn(name = "resource_id")
	@JsonManagedReference
	private ResourcesModel resourceDetails;
	
	/*@Column(name = "resource_id")
	private String resourceId;*/
	
public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	//	@Temporal(TemporalType.DATE)
	@Column(name = "date")
	private Date date;
	
//	@Temporal(TemporalType.TIME)
	@Column(name = "start_time")
	private Time startTime;
	
//	@Temporal(TemporalType.TIME)
	@Column(name = "end_time")
	private Time endTime;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "participants")
	private int numberOfParticipants;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
	private String description;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

	public String getBookingId() {
		return bookingId;
	}
	
	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}
	
	public UsersModel getUserDetails() {
		return userDetails;
	}
	
	public void setUserDetails(UsersModel userDetails) {
		this.userDetails = userDetails;
	}
	
	public ResourcesModel getResourceDetails() {
		return resourceDetails;
	}
	
	public void setResourceDetails(ResourcesModel resourceDetails) {
		this.resourceDetails = resourceDetails;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Time getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	
	public Time getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Time endTime) {
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
