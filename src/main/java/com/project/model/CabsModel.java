/**
 * To map the cabs table in the database in the ORM
 * @author Arpit Pittie
 */
package com.project.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "cabs")
public class CabsModel {

	@Id
	@Column(name = "cab_number")
	private String cabNumber;

	@Column(name = "driver_name")
	private String driverName;

	@Column(name = "driver_number")
	private long driverNumber;

	@Column(name = "route")
	private String route;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "resource_id")
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
