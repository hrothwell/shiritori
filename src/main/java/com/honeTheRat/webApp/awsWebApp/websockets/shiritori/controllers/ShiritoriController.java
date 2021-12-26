package com.honeTheRat.webApp.awsWebApp.websockets.shiritori.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.honeTheRat.webApp.awsWebApp.enums.AdminConstants;
import com.honeTheRat.webApp.awsWebApp.websockets.pojos.BasicMessage;
import com.honeTheRat.webApp.awsWebApp.websockets.shiritori.logic.ShiritoriGameLogic;
import com.honeTheRat.webApp.awsWebApp.websockets.shiritori.pojos.ShiritoriMessage;
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
