package com.titankingdoms.nodinchan.titanchat.participant;

import java.util.logging.Level;

import org.bukkit.ChatColor;

import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;

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
	public void send(MessageLevel level, String message) {
		String format = "[" + level.getColour() + "TitanChat" + ChatColor.WHITE + "] " + level.getColour() + "%msg";
		plugin.log(Level.INFO, format.replace("%msg", message));
	}
}