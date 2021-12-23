package com.honeTheRat.webApp.awsWebApp.websockets.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class CustomInterceptor implements ChannelInterceptor {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel){
		log.info("In CustomInterceptor. Message headers: {}", message.getHeaders().toString());
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		if(StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
			if(!validateSubscription(headerAccessor.getDestination())) {
				//Errors shows up in stomp client side
				throw new RuntimeException("Please use an alphanumeric room name. Your room path: " + headerAccessor.getDestination());
			}
		}
		return message;
	}
	
	private boolean validateSubscription(String roomName) {
		//If they send a roomName with a "/" in it it basically won't work, but we don't care. Their connection never opens really
		//strip "/" since regex wont let me escape it in .matches()
		String strippedRoomName = roomName.replaceAll("/", "");
		return strippedRoomName.matches("^[a-zA-Z0-9]+$");
	}
}
