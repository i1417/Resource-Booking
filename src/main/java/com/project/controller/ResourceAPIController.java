/**
 * Class to manage the Resource API 
 * @author Pratap Singh Ranawat and Vivek Mittal
 */
package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.service.ResourceService;
import com.project.util.Response;
import com.project.vo.ResourcesVO;

@Controller
public class ResourceAPIController {

	// To interact with the service layer
	@Autowired
	private ResourceService resourceService;

	// To get the beans
	@Autowired
	private ApplicationContext context;

	/**
	 * To get the list of all the available resources.
	 * @return - Response object showing the list of all available resources.
	 * @author Vivek Mittal, Pratap Singh
	 */
	@RequestMapping(value = "/resources/getAll", method = RequestMethod.GET)
	public @ResponseBody Response getAllResourceList() {
		// Getting the result from the Service Layer
		List<ResourcesVO> result = resourceService.allResourceList();

		// Sending back the response to the client
		if (result != null) {
			return new Response(200, result);
		} else {
			return new Response(400, "No Resource Available");
		}
	}

	/**
	 * Following function creates a new resource
	 * @param resourcesVO(ResourcesVO) - contains the details of the new resource
	 * @return Response object confirming the creation of new resource
	 * @author Vivek Mittal, Pratap Singh
	 */
	@RequestMapping(value = "/resources/createResource", method = RequestMethod.POST)
	public @ResponseBody Response createResource(
			@RequestBody ResourcesVO resourcesVO) {
		// Getting the result from the Service Layer
		boolean result = resourceService.createResource(resourcesVO);

		// Sending back the response to the client
		if (result) {
			return new Response(200, result);
		} else {
			return new Response(400, "Couldn't create a new resource");
		}
	}

	/**
	 * Following function edits a existing resource
	 * @param resourcesVO(ResourcesVO) - contains the details of the edited resource
	 * @return Response object confirming the updation of resource
	 * @author Vivek Mittal, Pratap Singh
	 */
	@RequestMapping(value = "/resources/editResource", method = RequestMethod.POST)
	public @ResponseBody Response editResource(
			@RequestBody ResourcesVO resourcesVO) {
		// Getting the result from the Service Layer
		boolean result = resourceService.editResource(resourcesVO);

		// Sending back the response to the client
		if (result) {
			return new Response(200, result);
		} else {
			return new Response(400, "Couldn't edit the existing resource");
		}
	}

}