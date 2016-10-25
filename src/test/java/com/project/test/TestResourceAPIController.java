package com.project.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.project.model.ResourcesVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml" })
@WebAppConfiguration
public class TestResourceAPIController {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private ResourcesVO resource;
	private ObjectMapper mapper;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(
				this.webApplicationContext).build();
		resource = new ResourcesVO();
		mapper = new ObjectMapper();
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testGetAllUsers() throws Exception {
		this.mockMvc.perform(
				get("/resources/getAll").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@Rollback(true)
	@Transactional
	public void testCreateResource() throws Exception {
		resource.setResourceName("B8F5C1");
		resource.setType("Conference");
		resource.setCapacity(30);
		resource.setResourceAdmins(new ArrayList());

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(resource);

		this.mockMvc.perform(
				post("/resources/createResource").contentType(
						MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@Rollback(true)
	@Transactional
	public void testEditResource() throws Exception {
		resource.setResourceId(102);
		resource.setResourceName("B3F2C1");
		resource.setType("Conference");
		resource.setCapacity(100);
		resource.setResourceAdmins(new ArrayList());

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(resource);

		this.mockMvc.perform(
				post("/resources/editResource").contentType(
						MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}

}
