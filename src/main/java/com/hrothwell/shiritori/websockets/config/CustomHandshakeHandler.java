package com.hrothwell.shiritori.websockets.config;

import java.security.Principal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * Used for assigning our "unknown" users a principal
 * @author hrothwell
 *
 */
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Override
	protected Principal determineUser(ServerHttpRequest request,
	        WebSocketHandler wsHandler, Map<String, Object> attributes) {
		log.info("In CustomHandshakeHandler");
	    Principal principal = request.getPrincipal();
	    
	    if (principal == null) {
	        principal = new AnonymousUserPrincipal();
	        //TODO if we give this principal the same name as another one, will it add it to the list of "connected" users? 
	        //apparently not, but it will still connect to another websocket? 
	        log.info("request remote address: {}", request.getRemoteAddress());
	        
	        String uniqueName = request.getRemoteAddress().getAddress().toString();
	        
	        ((AnonymousUserPrincipal) principal).setName(uniqueName);
	    }
	
	    return principal;
	}

}
