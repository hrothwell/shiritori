package com.hrothwell.shiritori.game.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.hrothwell.shiritori.game.pojos.ShiritoriGame;
import com.hrothwell.shiritori.game.pojos.ShiritoriMessage;

@Service
public class ShiritoriGameLogic {
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Map<String, ShiritoriGame> shiritoriGames;
	
	private RestTemplate rest; //todo just make a bean and use that? 
	
	
	public String validateWord(ShiritoriMessage gameMessage, String room) {
		//TODO validate that the game exists and wasn't removed, if removed, send them a message saying so
		if(shiritoriGames.get(room) == null) {
			return "Game no longer exists";
		}
		
		ShiritoriGame g = shiritoriGames.get(room);
		g.setTimeLastActive(new Date()); //received a message, so game is probably active
		StringBuilder replyMessage = new StringBuilder();
		String playedWord = StringUtils.trimWhitespace(gameMessage.getMessage()); //trim the word
		if(playedWord.split(" ").length > 1) {
			//more than one word, allow this? probably not
			return "Please play a single word";
		}
		
		char firstLetter = playedWord.charAt(0);
		char lastOfLastWord = g.getLastKnownWord().charAt(g.getLastKnownWord().length()-1);
		
		//TODO Do dictionary checks, verify is known and is a real word
		if(firstLetter == lastOfLastWord || lastOfLastWord == '?') {
			//check dictionary
			boolean validWord = checkDictionary(playedWord, replyMessage);
			//append message stuff to the "valid" string (nameley the definition)
			//valid
			if(validWord) {
				g.setLastKnownWord(playedWord);
				g.getSeenWords().add(playedWord);
				g.setLastKnownPlayer(gameMessage.getUserName()); 
			}
			
		}
		
		return replyMessage.toString();
	}
	
	//Message that is passed in is modified, boolean returned is whether or not the word was valid 
	private boolean checkDictionary(String word, StringBuilder message){
		rest = new RestTemplate();
		boolean foundValidWord = false;
		//This throws 404 if not found
		try {
			ResponseEntity<Object[]> r = rest.getForEntity("https://api.dictionaryapi.dev/api/v2/entries/en/{word}", Object[].class, word);
			HashMap<String, Object> map = (HashMap<String, Object>) r.getBody()[0];// first item returned
			List<HashMap<String, Object>> meanings = (List<HashMap<String, Object>>) map.get("meanings");
			for(HashMap<String, Object> m : meanings) {
				String partOfSpeech = (String) m.get("partOfSpeech");
				if(partOfSpeech.trim().equalsIgnoreCase("noun")) {
					//valid word
					List<HashMap<String, Object>> definitions = (List<HashMap<String, Object>>) m.get("definitions");
					//always just grab first def
					String def = (String) definitions.get(0).get("definition");
					message.append(word + ": " + def);
					foundValidWord = true; //found valid word
					break;
				}
			}
			
			if(!foundValidWord) {
				message.append("Found definitions of word '" + word + "' but none appear to be a noun");
			}
		}
		catch( HttpServerErrorException | HttpClientErrorException e) {
			log.error("Exception calling dictionary service. Status Code:{} \n Message: {}", e.getStatusCode(), e.getResponseBodyAsString());
			//Most likely a 404 word not found, or some other error
			if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				message.append( "'" + word + "' is not a word according to our dictionary"); 
			}
			else {
				message.append("Something unexpected happened: " + e.getMessage());
			}
		}
		
		
		return foundValidWord;
	}
}
