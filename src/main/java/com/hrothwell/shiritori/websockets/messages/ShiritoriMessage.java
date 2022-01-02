package com.hrothwell.shiritori.websockets.messages;

import org.owasp.encoder.Encode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShiritoriMessage extends BasicMessage {
	//username / message live in parent, the "message" should be their word
	
	//TODO this really isnt needed as we just use the destination there message is being sent to
	private String gameName; 
	
	//TODO what else would be needed? 
	
	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = Encode.forHtml(gameName);
	}
	
}
