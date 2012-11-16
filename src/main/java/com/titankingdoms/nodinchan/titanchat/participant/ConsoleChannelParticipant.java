package com.titankingdoms.nodinchan.titanchat.participant;

import java.util.logging.Level;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;

public class ConsoleChannelParticipant extends Participant {
	
	public ConsoleChannelParticipant() {
		super("#!");
	}
	
	@Override
	public void chat(Channel channel, String message) {
		
	}
	
	@Override
	public boolean hasPermission(String permission) {
		return true;
	}
	
	@Override
	public boolean isOnline() {
		return true;
	}
	
	@Override
	public void send(String... messages) {
		for (String message : messages)
			plugin.log(Level.INFO, message);
	}
}