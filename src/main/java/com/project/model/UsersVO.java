/**
 * Class to map the User View Object
 * @author Arpit Pittie
 */
package com.project.model;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UsersVO {

	private int employeeId;
	private String name;
	private String designation;
	private String email;
	private String password;
	private long mobileNumber;
	private String role;
	private List<ResourcesVO> adminOfResources;
	private List<BookingsVO> bookingsMade;

	public List<BookingsVO> getBookingsMade() {
		return bookingsMade;
	}

	public void setBookingsMade(List<BookingsVO> bookingsMade) {
		this.bookingsMade = bookingsMade;
	}

	public List<ResourcesVO> getAdminOfResources() {
		return adminOfResources;
	}

	public void setAdminOfResources(List<ResourcesVO> adminOfResources) {
		this.adminOfResources = adminOfResources;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
