package com.hrothwell.shiritori.websockets.config;

import java.security.Principal;
/**
 * Since most / all users are "anonymous," we need this to help store them in our SimpUserRegistry
 * @author hrothwell
 *
 */
public class AnonymousUserPrincipal implements Principal {
	private String name;
	
	@Override
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object p) {
		if(p instanceof Principal) {
			Principal principal = (Principal) p;
			return this.name.equals(principal.getName());
		}
		return false;
	}

}
