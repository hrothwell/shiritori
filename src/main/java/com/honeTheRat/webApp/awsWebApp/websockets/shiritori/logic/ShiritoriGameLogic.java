package com.honeTheRat.webApp.awsWebApp.websockets.shiritori.logic;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.honeTheRat.webApp.awsWebApp.websockets.shiritori.pojos.ShiritoriGame;
import com.honeTheRat.webApp.awsWebApp.websockets.shiritori.pojos.ShiritoriMessage;

@Service
public class ShiritoriGameLogic {

	@Autowired
	private Map<String, ShiritoriGame> shiritoriGames;
	
	//TODO validate that the game exists? 
	public String validateWord(ShiritoriMessage gameMessage) {
		String valid = "not valid";
		String playedWord = StringUtils.trimWhitespace(gameMessage.getMessage()); //trim the word
		if(playedWord.split(" ").length > 1) {
			//more than one word, allow this? probably not
		}
		
		//TODO check if no words yet played
		ShiritoriGame g = shiritoriGames.get(gameMessage.getGameName());
		char firstLetter = playedWord.charAt(0);
		char lastOfLastWord = g.getLastKnownWord().charAt(g.getLastKnownWord().length()-1);
		
		//TODO Do dictionary checks, verify is known and is a real word
		if(firstLetter == lastOfLastWord) {
			//valid
			valid = "valid";
			g.setLastKnownWord(playedWord);
			g.setLastKnownPlayer(gameMessage.getUserName()); 
		}
		
		return valid;
	}
}
