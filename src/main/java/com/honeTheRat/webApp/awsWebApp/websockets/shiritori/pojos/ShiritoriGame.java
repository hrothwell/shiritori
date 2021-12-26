package com.honeTheRat.webApp.awsWebApp.websockets.shiritori.pojos;

import java.util.HashSet;

public class ShiritoriGame {
	private String gameName; //name of the "room" they are in. Should this be some sort of hash? The key for the map this lives in will probably also be the gameName
	private String gamePassword; //password to enter the game? 
	private HashSet<String> seenWords;
	//players?
	private HashSet<String> players; //dunno if this is really needed tbh
	private String lastKnownWord; //last word that was played
	private String lastKnownPlayer; //See who last played a VALID word. what to store here? name? IP? 
	private boolean gameStarted; //if the game is currently ongoing
	
	public ShiritoriGame() {
		this.setGameStarted(false);
		this.setPlayers(new HashSet<>());
		this.setSeenWords(new HashSet<>());
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
	
}
