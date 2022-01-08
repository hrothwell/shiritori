package com.hrothwell.shiritori.websockets.messages;

import java.util.Set;

import com.hrothwell.shiritori.enums.AdminConstants;

/**
 * Message that should only be sent to users by the server, this should not be marked as an input/request body/etc 
 * for any messages that users send (ie do not put it in a mapping)
 * @author hrothwell
 *
 */
public class AdminOnlyMessage extends BasicMessage {
	private Set<String> playedWords; //words for the shiritori game this message is for

	public AdminOnlyMessage(String message, Set<String> words) {
		super(AdminConstants.UserName.getValue(), message, AdminConstants.AdminColor.getValue());
		this.setPlayedWords(words);
	}
	public Set<String> getPlayedWords() {
		return playedWords;
	}

	public void setPlayedWords(Set<String> playedWords) {
		this.playedWords = playedWords;
	}
}
