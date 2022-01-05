package com.hrothwell.shiritori.websockets.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.hrothwell.shiritori.enums.AdminConstants;
import com.hrothwell.shiritori.game.pojos.ShiritoriGame;
import com.hrothwell.shiritori.websockets.messages.BasicMessage;

/**
 * Listens to events on our sockets, used for when a user subscribes to a topic we send a message to everyone subscribed to that topic
 * @author hrothwell
 *
 */
@Component
public class WebSocketEventListener {
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private Map<String, ShiritoriGame> shiritoriGames;
	
	@EventListener
	public void handleSubscribeEvent(SessionSubscribeEvent event) {
		GenericMessage message = (GenericMessage) event.getMessage();
		String simpDestination = (String) message.getHeaders().get("simpDestination");
		
		//basically anytime a user subscribes to anything we alert the people already there
		if(simpDestination.startsWith("/topic/")) {
			BasicMessage m = new BasicMessage(AdminConstants.UserName.getValue(), AdminConstants.NewUserJoin.getValue(), AdminConstants.AdminColor.getValue());
			template.convertAndSend(simpDestination, m);
		}
	}
	
	@EventListener
	public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
		//TODO send out message
	}
	
	@EventListener
	public void handleDisconnectEvent(SessionDisconnectEvent event) {
		//TODO send out message
	}
}
