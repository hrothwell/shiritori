package com.honeTheRat.webApp.awsWebApp.enums;
/**
 * Various constants to just use for random message things (server sending to all clients kinda stuff)
 * @author hrothwell
 *
 */
public enum AdminConstants {
	UserName("Admin"),
	NewUserJoin("New user has joined the room");
	
	private String value;
	
	private AdminConstants(String value) {
		this.setValue(value);
	}
	
	public String getValue() {
		return this.value;
	}

	private void setValue(String value) {
		this.value = value;
	}
}
