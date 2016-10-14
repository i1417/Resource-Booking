/**
 * To map the users table in the database in the ORM
 * @author Arpit Pittie
 */
package com.project.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Component
@Table(name = "users")
public class UsersModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id")
	private int employeeId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "designation")
	private String designation;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "mobile_number")
	private long mobileNumber;
	
	@Column(name = "role")
	private String role;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "admins",
	joinColumns = {@JoinColumn(name = "employee_id", referencedColumnName = "employee_id")},
	inverseJoinColumns = {@JoinColumn(name = "resource_id", referencedColumnName = "resource_id")})
	private List<ResourcesModel> adminOfResources;
	
	@OneToMany(mappedBy = "userDetails")
	@JsonManagedReference
	private List<BookingsModel> bookingsMade;

	public List<BookingsModel> getBookingsMade() {
		return bookingsMade;
	}

	public void setBookingsMade(List<BookingsModel> bookingsMade) {
		this.bookingsMade = bookingsMade;
	}

	public List<ResourcesModel> getAdminOfResources() {
		return adminOfResources;
	}

	public void setAdminOfResources(List<ResourcesModel> adminOfResources) {
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
