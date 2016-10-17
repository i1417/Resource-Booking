package com.project.service;

/**
 * Class to provide a single API for the database interaction with the controllers
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.project.model.ResourcesVO;

@Service("resourceFacade")
public class ResourceFacade {

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ApplicationContext context; // To get the beans

	/**
	 * To get the list of all available resources from ResourceService class
	 * @return - List of all the available resources
	 */
	public List<ResourcesVO> allResourceList() {
		return resourceService.allResourceList();
	}

	/**
	 * Following function helps in creates a new resource
	 * @param resourcesVO contains the information regarding new resource.
	 * @return true/false whether resource created successfully or not.
	 */
	public boolean createResource(ResourcesVO resourcesVO) {
		
		
		return resourceService.createResource(resourcesVO);
	}

	/**
	 * Following function helps in deleting a existing resource
	 * @param resourcesVO contains the information regarding the resource to be deleted.
	 * @return true/false whether resource deleted successfully or not.
	 */
	public boolean deleteResource(ResourcesVO resourcesVO) {

		return resourceService.deleteResource(resourcesVO);
	}

	/**
	 * Following function helps in edits a existing resource
	 * @param resourcesVO contains the information regarding resource and edited data.
	 * @return true/false whether resource edited successfully or not.
	 */
	public boolean editResource(ResourcesVO resourcesVO) {
		return resourceService.editResource(resourcesVO);
	}
}