package com.honeTheRat.webApp.awsWebApp.websockets.shiritori.logic;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.honeTheRat.webApp.awsWebApp.websockets.shiritori.pojos.ShiritoriGame;
import com.honeTheRat.webApp.awsWebApp.websockets.shiritori.pojos.ShiritoriMessage;

@Service
public class ShiritoriGameLogic {

	@Autowired
	private Map<String, ShiritoriGame> shiritoriGames;
	
	private RestTemplate rest; //todo just make a bean and use that? 
	
	//TODO validate that the game exists? 
	public String validateWord(ShiritoriMessage gameMessage, String room) {
		String valid = "not valid";
		String playedWord = StringUtils.trimWhitespace(gameMessage.getMessage()); //trim the word
		if(playedWord.split(" ").length > 1) {
			//more than one word, allow this? probably not
			return valid;
		}
		
		//TODO check if no words yet played
		ShiritoriGame g = shiritoriGames.get(room);
		char firstLetter = playedWord.charAt(0);
		char lastOfLastWord = g.getLastKnownWord().charAt(g.getLastKnownWord().length()-1);
		
		//TODO Do dictionary checks, verify is known and is a real word
		if(firstLetter == lastOfLastWord || lastOfLastWord == '?') {
			//check dictionary
			checkDictionary(playedWord);
			//append message stuff to the "valid" string (nameley the definition)
			//valid
			valid = "valid";
			g.setLastKnownWord(playedWord);
			g.setLastKnownPlayer(gameMessage.getUserName()); 
		}
		
		return valid;
	}
	
	private Map<?, ?> checkDictionary(String word){
		rest = new RestTemplate();
		String response = rest.getForObject("https://api.dictionaryapi.dev/api/v2/entries/en/{word}", String.class, word);
		//TODO parse out response. Need to know how to map it correctly (probably just look at what is returned and go from there)
		//make object, or just use map? 
		return null;
	}
}
