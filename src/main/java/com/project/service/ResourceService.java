package com.project.service;

/**
 * Class To implement the service layer for the Users
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import com.project.model.ResourcesVO;

@Service("resourceService")
@Transactional
public class ResourceService {

	@Autowired
	private ResourceDAO resourceDAO;

	// To get the beans
	@Autowired
	private ApplicationContext context;

	/**
	 * To get the list of all available resources
	 * 
	 * @return - List of all the available resources
	 */
	public List<ResourcesVO> allResourceList() {
		return resourceDAO.allResourceList();
	}

}
