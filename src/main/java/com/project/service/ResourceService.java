package com.project.service;

/**
 * Class To implement the service layer for the Users
 * @author Pratap Singh Ranawat and Vivek Mittal
 */

import java.util.ArrayList;
import java.util.List;


import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.project.model.ResourcesModel;
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
	 * To get the list of all available resources from ResourcesDao class
	 * 
	 * @return - List of all the available resources
	 */
	
	public List<ResourcesVO> allResourceList() {

		List<ResourcesModel> resourcesModel = new ArrayList<ResourcesModel>();

		resourcesModel = resourceDAO.allResourceList();
		List<ResourcesVO> resourcesVO = new ArrayList<ResourcesVO>(
				resourcesModel.size());
		ResourcesVO resourceVO = context.getBean(ResourcesVO.class);
		System.out.println(" model data" + resourcesModel.size());
		for (int i = 0; i < resourcesModel.size(); i++) {
			BeanUtils.copyProperties(resourcesModel.get(i), resourceVO);
			resourcesVO.add(resourceVO);
		}

		System.out.println("vo data" + resourcesVO);
		return resourcesVO;
	}

}
