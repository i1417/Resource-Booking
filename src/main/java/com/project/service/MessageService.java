package com.project.service;

import java.net.*;
import java.io.*;

import org.springframework.stereotype.Component;

@Component
public class MessageService {
	
	private String userName;
	private String password;
	
	public MessageService(){
		this.setUserName("resourcebooking");
		this.setPassword("resourcebooking");
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

	static public void main(String[] args) {
		new MessageService().sendSMS("Hello amit how are you", "917821042473");
        
    }
    
    public void sendSMS(String messageBody,String receiver)
    {
    	try {
            // Construct data
            String data = "";
            /*
             * Note the suggested encoding for certain parameters, notably
             * the username, password and especially the message.  ISO-8859-1
             * is essentially the character set that we use for message bodies,
             * with a few exceptions for e.g. Greek characters.  For a full list,
             * see:  http://developer.bulksms.com/eapi/submission/character-encoding/
             */
            data += "username=" + URLEncoder.encode(this.getUserName(), "ISO-8859-1");
            data += "&password=" + URLEncoder.encode(this.getPassword(), "ISO-8859-1");
            data += "&message=" + URLEncoder.encode(messageBody, "ISO-8859-1");
            data += "&want_report=1";
            data += "&msisdn="+receiver;

            // Send data
            // Please see the FAQ regarding HTTPS (port 443) and HTTP (port 80/5567)
            URL url = new URL("https://bulksms.vsms.net/eapi/submission/send_sms/2/2.0");

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                // Print the response output...
                System.out.println(line);
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

