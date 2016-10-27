package com.project.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.project.vo.UsersVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml" })
@WebAppConfiguration
public class TestUserAPIController {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private UsersVO user;
	private ObjectMapper mapper;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(
				this.webApplicationContext).build();
		user = new UsersVO();
		mapper = new ObjectMapper();
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testCustomLoginStatus() throws Exception {
		user.setEmail("avinash.chopra@metacube.com");
		user.setPassword("4d09f5764e116ea1ff75527943be258f");

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(user);

		this.mockMvc.perform(
				post("/validate/custom")
						.contentType(MediaType.APPLICATION_JSON).content(
								jsonObject)).andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testCreateUserAccount() throws Exception {
		user.setEmail("yash.jain@metacube.com");
		user.setPassword("4d09f5764e116ea1ff75527943be258f");
		user.setDesignation("GET");
		user.setRole("user");
		user.setMobileNumber(Long.parseLong("6547893211"));

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(user);

		this.mockMvc.perform(
				post("/createAccount").contentType(MediaType.APPLICATION_JSON)
						.content(jsonObject)).andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testUpdateUserDetails() throws Exception {
		user.setEmail("vivek.mittal@metacube.com");
		user.setDesignation("Developer");
		user.setMobileNumber(Long.parseLong("998855644"));

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(user);

		this.mockMvc.perform(
				post("/user/update").contentType(MediaType.APPLICATION_JSON)
						.content(jsonObject)).andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testGetAllUsers() throws Exception {
		this.mockMvc.perform(
				get("/user/getAll").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testForgotPassword() throws Exception {
		user.setEmail("amit.sharma1@metacube.com");

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(user);

		this.mockMvc.perform(
				post("/forgotPass").contentType(MediaType.APPLICATION_JSON)
						.content(jsonObject)).andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testChangePassword() throws Exception {
		user.setEmail("amit.sharma@metacube.com");
		user.setPassword("4d09f5764e116ea1ff75527943be258f");

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(user);

		this.mockMvc.perform(
				post("/forgotPass?token=1000003").contentType(
						MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	@Transactional
	public void testSendInvitation() throws Exception {
		user.setEmail("pulkit.gupta@metacube.com");
		user.setName("Pulkit Gupta");

		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String jsonObject = ow.writeValueAsString(user);

		this.mockMvc.perform(
				post("/user/sendInvitationToUser").contentType(
						MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}

}
