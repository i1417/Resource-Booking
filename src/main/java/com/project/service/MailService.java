package com.project.service;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import com.project.model.UsersVO;

/**
 * @author Rohit Singhavi
 * Class to send email notifications
 */

@Component
public class MailService {
	private Properties javaMailProperties;
	private JavaMailSenderImpl mailSender;
	private SimpleMailMessage message;

	public void sendMail(UsersVO userDeatils, String mailSubject, String mailMessage) {

		mailSender = new JavaMailSenderImpl();
		message = new SimpleMailMessage();
		javaMailProperties = new Properties();

		//FQDN of SMTP Service
		mailSender.setHost("smtp.gmail.com");
		
		//port to access gmail smtp
		mailSender.setPort(587);
		
		//authentication of gmail account
		mailSender.setUsername("resourcebooking.project@gmail.com");
		mailSender.setPassword("resourcebookproject");

		
		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");

		mailSender.setJavaMailProperties(javaMailProperties);

		message.setFrom("resourcebooking.project@gmail.com");
		message.setTo(userDeatils.getEmail());
		message.setSubject(mailSubject);

		//MESSAGE BODY
		message.setText(String.format("Dear " + userDeatils.getName()
				+ ",\n\n"+mailMessage));
		
		mailSender.send(message);
	}

}
