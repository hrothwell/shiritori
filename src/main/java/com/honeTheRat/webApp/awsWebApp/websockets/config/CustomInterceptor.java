package com.honeTheRat.webApp.awsWebApp.websockets.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

public class CustomInterceptor implements ChannelInterceptor {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel){
		log.info("In CustomInterceptor. Message headers: {}", message.getHeaders().toString());
		//TODO headers contains the destination, which we could use to check validity of subscription?
		return message;
	}
	
	//TODO add a "validate" method to validate the "room" if they are subscribing
}
