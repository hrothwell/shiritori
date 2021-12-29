package com.hrothwell.shiritori.exceptions;

public class ShiritoriJoinException extends RuntimeException {
	
	private String message;
	
	public ShiritoriJoinException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
