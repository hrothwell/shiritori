package com.hrothwell.shiritori.websockets.messages;

import org.owasp.encoder.Encode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicMessage {
	
	private String userName;
	private String message;
	private String hexColor;
	
	public BasicMessage() {
		//empty for Jackson serialization
	}
	
	public BasicMessage(String userName, String message) {
		this.setUserName(userName);
		this.setMessage(message);
	}
	public BasicMessage(String userName, String message, String hexColor) {
		this.setUserName(userName);
		this.setMessage(message);
		this.setHexColor(hexColor);
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = Encode.forHtml(userName);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = Encode.forHtml(message);
	}
	
	public String getHexColor() {
		return hexColor;
	}

	public void setHexColor(String hexColor) {
		this.hexColor = Encode.forHtml(hexColor);;
	}
	
}
