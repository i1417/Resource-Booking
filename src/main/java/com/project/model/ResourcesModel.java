/**
 * To map the resources table in the database in the ORM
 * @author Pratap Singh Ranawat and Vivek Mittal
 */
package com.project.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Component
@Table(name = "resources")
public class ResourcesModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "resource_id")
	private int resourceId;
	
	@Column(name = "resource_name")
	private String resourceName;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "capacity")
	private int capacity;
	
	@ManyToMany(mappedBy = "adminOfResources")/*cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "admins",
	joinColumns = {@JoinColumn(name = "resource_id", referencedColumnName = "resource_id")},
	inverseJoinColumns = {@JoinColumn(name = "employee_id", referencedColumnName = "employee_id")})*/
	@JsonIgnore
	private List<UsersModel> resourceAdmins;
	
	@OneToMany(mappedBy = "resourceDetails")
	@JsonBackReference
	private List<BookingsModel> bookedList;
	
	public List<BookingsModel> getBookedList() {
		return bookedList;
	}

	public void setBookedList(List<BookingsModel> bookedList) {
		this.bookedList = bookedList;
	}

	public List<UsersModel> getResourceAdmins() {
		return resourceAdmins;
	}

	public void setResourceAdmins(List<UsersModel> resourceAdmins) {
		this.resourceAdmins = resourceAdmins;
	}

	public int getResourceId() {
		return resourceId;
	}
	
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	
	public String getResourceName() {
		return resourceName;
	}
	
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
