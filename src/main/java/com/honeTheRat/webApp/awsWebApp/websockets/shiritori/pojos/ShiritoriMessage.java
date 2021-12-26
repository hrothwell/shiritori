package com.honeTheRat.webApp.awsWebApp.websockets.shiritori.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.honeTheRat.webApp.awsWebApp.websockets.pojos.BasicMessage;
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
