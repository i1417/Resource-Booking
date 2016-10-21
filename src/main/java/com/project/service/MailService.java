/**
 * @author Rohit Singhavi Arpit Pittie
 * Class to send email notifications
 */
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

@Component
public class MailService {
	private Properties javaMailProperties;

	public void sendMail(UsersVO userDeatils, String mailSubject,
			String mailMessage) {

		setProperties();
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		SimpleMailMessage message = new SimpleMailMessage();

		// FQDN of SMTP Service
		mailSender.setHost("smtp.gmail.com");

		// port to access gmail smtp
		mailSender.setPort(587);

		// authentication of gmail account
		mailSender.setUsername("resourcebooking.project@gmail.com");
		mailSender.setPassword("resourcebookproject");

		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");

		mailSender.setJavaMailProperties(javaMailProperties);

		message.setFrom("resourcebooking.project@gmail.com");
		message.setTo(userDeatils.getEmail());
		message.setSubject(mailSubject);

		// MESSAGE BODY
		message.setText(String.format("Dear " + userDeatils.getName() + ",\n\n"
				+ mailMessage));

		mailSender.send(message);
	}

	public void sendHTMLMail(UsersVO userDeatils, String mailSubject,
			String mailMessage) throws MessagingException {

		setProperties();
		Session mailSession = Session.getDefaultInstance(javaMailProperties,
				null);
		MimeMessage emailMessage = new MimeMessage(mailSession);
		emailMessage.addRecipients(Message.RecipientType.TO,
				userDeatils.getEmail());

		emailMessage.setSubject(mailSubject);
		emailMessage.setContent(mailMessage, "text/html");

		String emailHost = "smtp.gmail.com";
		String fromUser = "resourcebooking.project";// just the id alone without
													// @gmail.com
		String fromUserEmailPassword = "resourcebookproject";

		Transport transport = mailSession.getTransport("smtp");

		transport.connect(emailHost, fromUser, fromUserEmailPassword);
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		System.out.println("Email sent successfully.");
	}

	private void setProperties() {
		javaMailProperties = new Properties();

		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
	}

}