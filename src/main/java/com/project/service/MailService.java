/**
 * @author Rohit Singhavi Arpit Pittie
 * Class to send email notifications
 * edited By- Amit Sharma-- removed hard coded email credentials 
 */
package com.project.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

	/**
	 * sends a mail to the user when any booking change or when a new booking is
	 * made
	 * 
	 * @param userDetails
	 *            -- user to whom mail is to be sent
	 * @param mailSubject
	 *            -- subject of the mail
	 * @param mailMessage
	 *            -- message body
	 */
	public void sendHTMLMail(UsersVO userDetails, String mailSubject,String mailMessage) {

		setProperties();
		Session mailSession = Session.getDefaultInstance(javaMailProperties,
				null);
		MimeMessage emailMessage = new MimeMessage(mailSession);
		
		try {
			emailMessage.addRecipients(Message.RecipientType.TO,
					userDetails.getEmail());
			emailMessage.setSubject(mailSubject);
			emailMessage.setContent(mailMessage, "text/html");

			String emailHost = getPropValues("host");
			String fromUser = getPropValues("emailUserName"); // just the id alone without @gmail.com
			String fromUserEmailPassword = getPropValues("emailPassword");
			Transport transport = mailSession.getTransport("smtp");
			

			transport.connect(emailHost, fromUser, fromUserEmailPassword);
			transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * set the javaMail Properties
	 */
	private void setProperties() {
		javaMailProperties = new Properties();

		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
	}

	/**
	 * retrieve the values of passed parameters from a properties file
	 * 
	 * @param propertyName
	 *            -the parameter whose value is to be retrieved
	 * @return-the value of parameter requested
	 */
	public String getPropValues(String propertyName) {
		String result = "";
		InputStream inputStream = null;

		try {
			Properties properties = new Properties();
			String propFileName = "dbconfig.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(
					propFileName);

			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			}

			result = properties.getProperty(propertyName);
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		return result;
	}

}