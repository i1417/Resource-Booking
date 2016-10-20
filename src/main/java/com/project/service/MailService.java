package com.project.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

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
	
	public void sendHTMLMail(UsersVO userDeatils, String mailSubject, String mailMessage) throws MessagingException {
		javaMailProperties = new Properties();
		
		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");

		
		Session mailSession = Session.getDefaultInstance(javaMailProperties, null);
		MimeMessage emailMessage = new MimeMessage(mailSession);	
		emailMessage.addRecipients(Message.RecipientType.TO, userDeatils.getEmail());
		
		mailMessage = "<form action='http://localhost:8080/Project-Authentication/' method='GET'>"
				+ "<input type='text' value='Approved' hidden/>"
				+ "<button style='background-color:#5cb85c; border-radius:5px; padding: 8px; color:white; border:1px solid;' value='Accepted' id='status'>Accept</button>"
				+ "</form>"
				+ "<form action='http://localhost:8080/Project-Authentication/' method='GET'>"
				+ "<input type='text' value='Rejected' hidden/>"
				+ "<button style='background-color:#d9534f; border-radius:5px; padding: 8px; color:white; border:1px solid;' value='Rejected' id='status'>Reject</button>"
				+ "</form>";
		
		emailMessage.setSubject(mailSubject);
		emailMessage.setContent(mailMessage, "text/html");
		
		String emailHost = "smtp.gmail.com";
		String fromUser = "resourcebooking.project";//just the id alone without @gmail.com
		String fromUserEmailPassword = "resourcebookproject";
		
		Transport transport = mailSession.getTransport("smtp");
		
		transport.connect(emailHost, fromUser, fromUserEmailPassword);
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		System.out.println("Email sent successfully.");
	}
	
	public static void main(String[] args) throws MessagingException {
		UsersVO u = new UsersVO();
		u.setEmail("anant.sharma@company.com");
		
		MailService ms= new MailService();
		ms.sendHTMLMail(u, "Testing Mail", "");
		
	}

}