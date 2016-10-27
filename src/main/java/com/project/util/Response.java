/**
 * To wrap the response
 * @author Arpit Pittie
 */
package com.project.util;

public class Response {

	private int status; // Setting the status code for the request
	private String message; // Setting the error against the request if any
	private Object data; // Setting the data to be given against the request

	public Response(Object data) {
		status = 0;
		this.data = data;
	}

	public Response(int status, Object data) {
		super();
		this.status = status;
		this.data = data;
	}

	public Response(int errorCode, String message) {
		status = errorCode;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return message;
	}

	public void setErrorMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
