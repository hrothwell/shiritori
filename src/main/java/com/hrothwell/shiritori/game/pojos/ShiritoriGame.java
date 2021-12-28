package com.hrothwell.shiritori.game.pojos;

import java.util.Date;
import java.util.HashSet;

public class ShiritoriGame {
	//Should this be some sort of hash? This is the key for the map the game is in
	private String gameName; 
	//password to enter the game
	private String gamePassword; 
	private HashSet<String> seenWords;
	private HashSet<String> players; 
	private String lastKnownWord; 
	//See who last played a VALID word. what to store here? name? IP? 
	private String lastKnownPlayer; 
	//if the game is currently ongoing (not yet implemented)
	private boolean gameStarted; 
	private Date timeLastActive;
	
	public ShiritoriGame() {
		this.setGameStarted(false);
		this.setPlayers(new HashSet<>());
		this.setSeenWords(new HashSet<>());
		this.setLastKnownPlayer("?");
		this.setLastKnownWord("?");
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGamePassword() {
		return gamePassword;
	}
	public void setGamePassword(String gamePassword) {
		this.gamePassword = gamePassword;
	}
	public HashSet<String> getSeenWords() {
		return seenWords;
	}
	public void setSeenWords(HashSet<String> seenWords) {
		this.seenWords = seenWords;
	}
	public HashSet<String> getPlayers() {
		return players;
	}
	public void setPlayers(HashSet<String> players) {
		this.players = players;
	}
	public String getLastKnownPlayer() {
		return lastKnownPlayer;
	}
	public void setLastKnownPlayer(String lastKnownPlayer) {
		this.lastKnownPlayer = lastKnownPlayer;
	}
	public String getLastKnownWord() {
		return lastKnownWord;
	}
	public void setLastKnownWord(String lastKnownWord) {
		this.lastKnownWord = lastKnownWord;
	}
	public boolean isGameStarted() {
		return gameStarted;
	}
	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}
	public Date getTimeLastActive() {
		return timeLastActive;
	}
	public void setTimeLastActive(Date timeLastActive) {
		this.timeLastActive = timeLastActive;
	}
	
}
