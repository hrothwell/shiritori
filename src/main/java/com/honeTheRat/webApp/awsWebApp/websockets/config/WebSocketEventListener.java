package com.honeTheRat.webApp.awsWebApp.websockets.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.honeTheRat.webApp.awsWebApp.enums.AdminConstants;
import com.honeTheRat.webApp.awsWebApp.websockets.pojos.BasicMessage;

/**
 * Listens to events on our sockets, used for when a user subscribes to a topic we send a message to everyone subscribed to that topic
 * @author hrothwell
 *
 */
@Component
public class WebSocketEventListener {
	@Autowired
	private SimpMessagingTemplate template;
	@EventListener
	public void handleSubscribeEvent(SessionSubscribeEvent event) {
		GenericMessage message = (GenericMessage) event.getMessage();
		String simpDestination = (String) message.getHeaders().get("simpDestination");
		if(simpDestination.startsWith("/topic/")) {
			BasicMessage m = new BasicMessage(AdminConstants.UserName.getValue(), AdminConstants.NewUserJoin.getValue());
			template.convertAndSend(simpDestination, m);
		}
	}
}
