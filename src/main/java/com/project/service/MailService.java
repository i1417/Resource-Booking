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

import org.springframework.stereotype.Component;

import com.project.model.UsersVO;

@Component
public class MailService {
	private Properties javaMailProperties;

	public void sendHTMLMail(UsersVO userDeatils, String mailSubject,
			String mailMessage) {

		setProperties();
		Session mailSession = Session.getDefaultInstance(javaMailProperties,
				null);
		MimeMessage emailMessage = new MimeMessage(mailSession);
		try {
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
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		System.out.println("Email sent successfully.");
	}

	private void setProperties() {
		javaMailProperties = new Properties();

		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
	}

}