package com.hrothwell.shiritori.controllers;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrothwell.shiritori.enums.AdminConstants;
import com.hrothwell.shiritori.exceptions.ShiritoriJoinException;
import com.hrothwell.shiritori.game.pojos.CreateAndJoinGameBody;
import com.hrothwell.shiritori.game.pojos.ShiritoriGame;
import com.hrothwell.shiritori.websockets.messages.BasicMessage;

/**
 * Controller for creating/joining a Shiritori
 * TODO: would probably be better to manage users via sessions/ip address/something. Right now we trust them to send a "name" on each message for the game. But also, it doesn't
 * really matter! This isn't meant to be a super secure thing with robust rules
 * @author hrothwell
 *
 */
@Controller
public class ShiritoriGameCreateAndJoin {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private SimpMessagingTemplate template;
	@Autowired
	private Map<String, ShiritoriGame> shiritoriGames;
	
	@PostMapping("/shiritori/createGame")
	public String createShiritoriGame(@RequestBody CreateAndJoinGameBody body, Model model) {
		
		//if name is not alphanumeric we have issues subscribing to their websocket as we only allow alphanumeric in CustomInterceptor
		if(!body.getGameName().matches("^[a-zA-Z0-9]+$")) {
			throw new ShiritoriJoinException("Please use alphanumeric game names");
		}
		if(body.getUserName().isBlank()) {
			body.setUserName("unknown");
		}
		
		//make game object, pass into view
		if(shiritoriGames.get(body.getGameName()) == null) {
			//make game, attach to model
			ShiritoriGame newGame = new ShiritoriGame();
			newGame.setGameName(body.getGameName());
			//TODO if it is null will that affect joining? 
			newGame.setGamePassword(StringUtils.trim(body.getPassword()));
			
			newGame.setTimeLastActive(new Date()); 
			shiritoriGames.put(newGame.getGameName(), newGame);
			model.addAttribute("pageTitle", newGame.getGameName());
			model.addAttribute("game", newGame);
			model.addAttribute("userName", body.getUserName());
		}
		else {
			//game already exists, just connect to existing game for now
			model.addAttribute("game", shiritoriGames.get(body.getGameName()));
			model.addAttribute("pageTitle", shiritoriGames.get(body.getGameName()));
			model.addAttribute("userName", body.getUserName());
		}
		//returns just a fragment, kinda cool
		return "shiritori.html :: shiritori"; 
	}
	
	@GetMapping("/shiritori/joinGame/{gameName}")
	public String joinShiritoriGame(@PathVariable String gameName, @RequestParam(name="userName", required=false, defaultValue="NO_NAME") String userName,
			@RequestParam(name="password", required=false, defaultValue="") String password, Model model) {
		ShiritoriGame g = shiritoriGames.get(gameName);
		if(g != null && g.getGamePassword().equals(password)) {
			model.addAttribute("game", g);
			model.addAttribute("pageTitle", g.getGameName());
			model.addAttribute("userName", userName);
			return "shiritori.html :: shiritori";
		}
		else {
			throw new ShiritoriJoinException("Incorrect password or game does not exist!");
		}
	}
	
	@GetMapping("/shiritori/findGames")
	public String findShiritoriGames(Model model) {
		model.addAttribute("shiritoriGames", shiritoriGames);
		return "fragments/shiritoriFragments.html :: findGames";
	}
	
	//manages removing inactive games
	//run every five minutes 
	@Scheduled(fixedRate = 300000)
	public void cleanUpGames() {
		log.info("checking for inactive games");
        Iterator<Map.Entry<String, ShiritoriGame>> itr = shiritoriGames.entrySet().iterator();
         
        while(itr.hasNext()){
             Map.Entry<String, ShiritoriGame> entry = itr.next();
             log.info("checking game {} for inactivity", entry.getKey());
             Date now = new Date();
             long elapsedTime = now.getTime() - entry.getValue().getTimeLastActive().getTime();
             
             if(TimeUnit.MILLISECONDS.toMinutes(elapsedTime) > 5) {
            	 log.info("Inactive game found: {}", entry.getKey());
            	 itr.remove();
            	 template.convertAndSend("/topic/shiritori/" + entry.getKey(), new BasicMessage(AdminConstants.UserName.name(), "Closing game due to inactivity"));
             }
             else {
            	 log.info("Game: {} - Active", entry.getKey());
             }
        }
		
	}
	
}
