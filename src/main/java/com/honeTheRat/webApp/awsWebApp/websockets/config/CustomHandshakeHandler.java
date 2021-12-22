package com.honeTheRat.webApp.awsWebApp.websockets.config;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * Used for assigning our "unknown" users a principal
 * @author hrothwell
 *
 */
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
	
	@Override
	protected Principal determineUser(ServerHttpRequest request,
	        WebSocketHandler wsHandler, Map<String, Object> attributes) {
		
	    Principal principal = request.getPrincipal();           
	    if (principal == null) {
	        principal = new AnonymousUserPrincipal();
	
	        String uniqueName = UUID.randomUUID().toString();
	
	        ((AnonymousUserPrincipal) principal).setName(uniqueName);
	    }
	
	    return principal;
	
	}

}
