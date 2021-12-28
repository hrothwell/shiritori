package com.hrothwell.shiritori.websockets.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.hrothwell.shiritori.enums.AdminConstants;
import com.hrothwell.shiritori.game.logic.ShiritoriGameLogic;
import com.hrothwell.shiritori.game.pojos.ShiritoriMessage;
import com.hrothwell.shiritori.websockets.pojos.BasicMessage;
@Controller
public class ShiritoriController {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ShiritoriGameLogic shiritoriGameLogic;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/shiritori/{room}")
	@SendTo("/topic/shiritori/{room}")
	public BasicMessage sendAndReceiveShiritori(ShiritoriMessage m, @DestinationVariable String room) throws Exception{
		simpMessagingTemplate.convertAndSend("/topic/shiritori/" + room, m);//send the current user's message to show up for everyone
		String validWord = shiritoriGameLogic.validateWord(m, room);
		
		BasicMessage reply = new BasicMessage(AdminConstants.UserName.getValue(), validWord);
		
		return reply;
	}
}