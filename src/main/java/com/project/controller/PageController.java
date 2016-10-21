/**
 * To map the pages with the URL
 * @author Arpit Pittie
 */
package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	/**
	 * To map the landing page of the website
	 * 
	 * @return - The name of the landing page
	 */
	@RequestMapping("/")
	public String showIndexPage() {
		return "index.html";
	}

	/*
	 * @RequestMapping("/homeAdmin") public String showLoginHomePage() { return
	 * "admin/index.html"; }
	 */
}
