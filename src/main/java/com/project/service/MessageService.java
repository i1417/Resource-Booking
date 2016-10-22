package com.project.service;

import java.net.*;
import java.util.Properties;
import java.io.*;

import org.springframework.stereotype.Component;

/**
 * 
 * send mobile sms to the user on booking status
 * @author Amit
 *
 */
@Component
public class MessageService {
	
	//username for sms api gateway
	private String userName;
	
	//password for sms api gateway
	private String password;
	
	//constructor
	public MessageService(){
		
		//set sms api credentials
		this.setUserName(getPropValues("smsAPIUsername"));
		this.setPassword(getPropValues("smsAPIPassword"));
	}

    /**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
    
	/**
	 * sends a message to a user on booking status
	 * @param messageBody--the sms body
	 * @param receiver--mobile number of the receiver with country code
	 */
    public void sendSMS(String messageBody,String receiver)
    {
    	try {
            // Construct data
    		String data = "";
            data += "username=" + URLEncoder.encode(this.getUserName(), "ISO-8859-1");
            data += "&password=" + URLEncoder.encode(this.getPassword(), "ISO-8859-1");
            data += "&message=" + URLEncoder.encode(messageBody, "ISO-8859-1");
            data += "&want_report=1";
            data += "&msisdn="+receiver;

            // sms api url
            URL url = new URL("https://bulksms.vsms.net/eapi/submission/send_sms/2/2.0");

            //call the sms api
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
            outputStreamWriter.write(data);
            outputStreamWriter.flush();

            // Get the response
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            bufferedReader.close();
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

			 inputStream= getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '"+ propFileName + "' not found in the classpath");
			}

		result = properties.getProperty(propertyName);
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
    
}

