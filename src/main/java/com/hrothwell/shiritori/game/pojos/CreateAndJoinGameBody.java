package com.hrothwell.shiritori.game.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAndJoinGameBody {
	private String gameName;
	private String password;
	//name of user making the room
	private String userName;
	
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String hostName) {
		this.userName = hostName;
	}
}
