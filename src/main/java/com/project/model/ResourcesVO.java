/**
 * Class to map the Resource View Object
 * @author Arpit Pittie
 */
package com.project.model;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ResourcesVO {

	@Override
	public String toString() {
		return "ResourcesVO [resourceId=" + resourceId + ", resourceName="
				+ resourceName + ", type=" + type + ", capacity=" + capacity
				+ ", resourceAdmins=" + resourceAdmins + ", bookedList="
				+ bookedList + "]";
	}

	private int resourceId;
	private String resourceName;
	private String type;
	private int capacity;
	private List<UsersVO> resourceAdmins;
	private List<BookingsVO> bookedList;
	
	public List<BookingsVO> getBookedList() {
		return bookedList;
	}

	public void setBookedList(List<BookingsVO> bookedList) {
		this.bookedList = bookedList;
	}

	public List<UsersVO> getResourceAdmins() {
		return resourceAdmins;
	}

	public void setResourceAdmins(List<UsersVO> resourceAdmins) {
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
