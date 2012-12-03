package com.titankingdoms.nodinchan.titanchat.core.channel;

import java.util.Set;

import com.titankingdoms.nodinchan.titanchat.participant.Participant;

public interface ChatHandler {
	
	public String getFormat();
	
	public Set<Participant> getRecipients(Participant sender, String message);
}