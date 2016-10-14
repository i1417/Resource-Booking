package com.project.controller;

/**
 * Class to manage the Resource API 
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.project.model.ResourcesVO;
import com.project.service.ResourceFacade;

@Controller
public class ResourceAPIController {

	// To interact with the facade layer
	@Autowired
	private ResourceFacade resourceFacade;

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
		List<ResourcesVO> result = resourceFacade.allResourceList();

		// Sending back the response to the client
		if (result != null) {
			System.out.println("OK");
			return new Response(200, result);
		} else {
			System.out.println("Wrong");
			return new Response(400, "No Resource Available");
		}
	}
}
