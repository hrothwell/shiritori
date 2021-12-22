package com.honeTheRat.webApp.awsWebApp.websockets.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeTheRat.webApp.awsWebApp.websockets.pojos.BasicMessage;

@Controller
public class TestChatController {
	private Logger log = LoggerFactory.getLogger(getClass());
	//TODO : Is there a potential memory leak when a user changes rooms but does so in a way to bypass our stompClient.disconnect() call in js? 
	//While testing using our /admin/socketConnections endpoint, it will say there are more users connected as someone has multiple connections open
	@MessageMapping("/{room}")
	@SendTo("/topic/{room}")
	public BasicMessage sendAndReceive(BasicMessage m, @DestinationVariable String room) throws Exception{
		log.info("Received message to room: {}", room);
		//TODO: Is there a way for us to verify that their room fits a certain pattern? ie only allow alpha numeric or something? 
		Thread.sleep(500); 
		return m;
	}
	
	@Autowired private SimpUserRegistry userRegistry;
	@GetMapping("/admin/socketConnections")
	public String getSocketConnectionsJSON(Model model) {
		//initialize to empty object to start with
		String json = "{}";
		try {
			json = new ObjectMapper().writeValueAsString(userRegistry.getUserCount());
		}
		catch(JsonProcessingException e) {
			log.error("Error parsing json");
		}
		log.info("JSON: {}", json);
		model.addAttribute("json",json);
		return "jsonResult.txt";
	}
}
