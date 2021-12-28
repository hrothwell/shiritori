package com.hrothwell.shiritori.websockets.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShiritoriMessage extends BasicMessage {
	//username / message live in parent, the "message" should be their word
	private String gameName; //the game this message is for. is this really needed though? could map to controller? 
	
	//TODO what else would be needed? 
	
	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
}
