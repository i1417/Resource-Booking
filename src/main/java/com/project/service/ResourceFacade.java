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
}
