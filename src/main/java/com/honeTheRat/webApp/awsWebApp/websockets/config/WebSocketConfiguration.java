package com.honeTheRat.webApp.awsWebApp.websockets.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic", "/queue"); // is this just a standard? enables a simple memory-based message broker to send messages back to users
		config.setApplicationDestinationPrefixes("/app"); // /app means it will look for annotated methods with @MessageMapping
		log.info("Finished setting up MessageBrokerRegistry");
	}
	
	/**
	 * enables SockJS fallback options, basically used if websockets are not available
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//TODO HRR: not sure what this really does tbh
		registry.addEndpoint("/gs-guide-websocket")
		.setHandshakeHandler(new CustomHandshakeHandler())
		.withSockJS();
		log.info("Finished setting up StompEndpointRegistry");
	}
}
