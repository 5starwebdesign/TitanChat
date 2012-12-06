package com.titankingdoms.nodinchan.titanchat.core.participant;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.ChatHandler;

public final class ConsoleChannelParticipant extends Participant {
	
	public ConsoleChannelParticipant() {
		super("CONSOLE");
	}
	
	public ChatHandler getChatHandler() {
		return null;
	}
	
	@Override
	public CommandSender getCommandSender() {
		return plugin.getServer().getConsoleSender();
	}
	
	@Override
	public boolean hasPermission(String permission) {
		return true;
	}
	
	@Override
	public boolean isMuted(String channel) {
		return false;
	}
	
	@Override
	public boolean isOnline() {
		return true;
	}
	
	@Override
	public void mute(String channel, boolean mute) {}
	
	@Override
	public void send(String... messages) {
		for (String message : messages)
			plugin.log(Level.INFO, message);
	}
}