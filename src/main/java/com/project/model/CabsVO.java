/**
 * Class to map the Cabs View Object
 * @author Arpit Pittie
 */
package com.project.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CabsVO {

	private String cabNumber;
	private String driverName;
	private long driverNumber;
	private String route;
	
	private ResourcesModel resource;
	
	public String getCabNumber() {
		return cabNumber;
	}

	public void setCabNumber(String cabNumber) {
		this.cabNumber = cabNumber;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public long getDriverNumber() {
		return driverNumber;
	}

	public void setDriverNumber(long driverNumber) {
		this.driverNumber = driverNumber;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public ResourcesModel getResource() {
		return resource;
	}

	public void setResource(ResourcesModel resource) {
		this.resource = resource;
	}
}
