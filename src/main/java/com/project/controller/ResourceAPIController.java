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

import com.project.model.ResourcesVO;
import com.project.service.ResourceService;

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
	 * 
	 * @return - Response object showing the list of all available resources.
	 */
	@RequestMapping(value = "/resources/getAll", method = RequestMethod.GET)
	public @ResponseBody Response getAllResourceList() {
		// Getting the result from the facade
		List<ResourcesVO> result = resourceService.allResourceList();

		// Sending back the response to the client
		if (result != null) {
			System.out.println("OK");
			return new Response(200, result);
		} else {
			System.out.println("Wrong");
			return new Response(400, "No Resource Available");
		}
	}

	/**
	 * Following function creates a new resource
	 * 
	 * @param resourcesVO
	 *            contains the details of the new resource
	 * @return Response object confirming the creation of resource
	 */
	@RequestMapping(value = "/resources/createResource", method = RequestMethod.POST)
	public @ResponseBody Response createResource(
			@RequestBody ResourcesVO resourcesVO) {
		// Getting the result from the facade
		boolean result = resourceService.createResource(resourcesVO);

		// Sending back the response to the client
		if (result) {
			System.out.println("OK");
			return new Response(200, result);
		} else {
			System.out.println("Wrong");
			return new Response(400, "No Resource Available");
		}
	}

	/**
	 * Following function deletes a resource
	 * 
	 * @param resourcesVO
	 *            contains the details of the new resource
	 * @return Response object confirming the deletion of resource
	 */
	/*
	 * @RequestMapping(value = "/resources/deleteResource", method =
	 * RequestMethod.POST) public @ResponseBody Response
	 * deleteResource(@RequestBody ResourcesVO resourcesVO) { // Getting the
	 * result from the facade boolean result =
	 * resourceFacade.deleteResource(resourcesVO);
	 * 
	 * // Sending back the response to the client if (result) {
	 * System.out.println("OK"); return new Response(200, result); } else {
	 * System.out.println("Wrong"); return new Response(400,
	 * "No Resource Available"); } }
	 */

	/**
	 * Following function edits a existing resource
	 * 
	 * @param resourcesVO
	 *            contains the details of the new resource
	 * @return Response object confirming the updation of resource
	 */
	@RequestMapping(value = "/resources/editResource", method = RequestMethod.POST)
	public @ResponseBody Response editResource(
			@RequestBody ResourcesVO resourcesVO) {
		// Getting the result from the facade
		boolean result = resourceService.editResource(resourcesVO);

		// Sending back the response to the client
		if (result) {
			System.out.println("OK");
			return new Response(200, result);
		} else {
			System.out.println("Wrong");
			return new Response(400, "No Resource Available");
		}
	}

}