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
import com.hrothwell.shiritori.websockets.messages.ShiritoriMessage;
/**
 * @author hrothwell
 *
 */
@Service
public class ShiritoriGameLogic {
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Map<String, ShiritoriGame> shiritoriGames;
	
	//TODO just make a bean and use that? 
	private RestTemplate rest; 
	
	public String validateWord(ShiritoriMessage gameMessage, String room) {
		if(shiritoriGames.get(room) == null) {
			return "Game no longer exists";
		}
		
		String playedWord = StringUtils.trimWhitespace(gameMessage.getMessage());
		if(playedWord.split(" ").length > 1) {
			return "Please play a single word";
		}
		
		ShiritoriGame g = shiritoriGames.get(room);
		g.setTimeLastActive(new Date()); 
		StringBuilder replyMessage = new StringBuilder();
		
		playedWord = playedWord.toLowerCase();
		char firstLetter = playedWord.charAt(0);
		char lastOfLastWord = g.getLastKnownWord().toLowerCase().charAt(g.getLastKnownWord().length()-1);
		
		if(firstLetter == lastOfLastWord || lastOfLastWord == '?') {
			boolean validWord = checkDictionary(playedWord, replyMessage);
			if(validWord) {
				g.setLastKnownWord(playedWord);
				g.getSeenWords().add(playedWord);
				g.setLastKnownPlayer(gameMessage.getUserName()); 
			}
		}
		else {
			String letter = new String(new char[] {lastOfLastWord});
			return "Next word must start with " + letter;
		}
		
		return replyMessage.toString();
	}
	
	private boolean checkDictionary(String word, StringBuilder message){
		rest = new RestTemplate();
		boolean foundValidWord = false;
		try {
			ResponseEntity<Object[]> r = rest.getForEntity("https://api.dictionaryapi.dev/api/v2/entries/en/{word}", Object[].class, word);
			HashMap<String, Object> map = (HashMap<String, Object>) r.getBody()[0];
			List<HashMap<String, Object>> meanings = (List<HashMap<String, Object>>) map.get("meanings");
			for(HashMap<String, Object> m : meanings) {
				String partOfSpeech = (String) m.get("partOfSpeech");
				if(partOfSpeech.trim().equalsIgnoreCase("noun")) {
					//valid word
					List<HashMap<String, Object>> definitions = (List<HashMap<String, Object>>) m.get("definitions");
					String def = (String) definitions.get(0).get("definition");
					message.append(word + " - " + def);
					foundValidWord = true; 
					break;
				}
			}
			
			if(!foundValidWord) {
				message.append("Found definitions for '" + word + "' but none appear to be a noun");
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
