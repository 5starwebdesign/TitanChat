package com.titankingdoms.nodinchan.titanchat.core.participant;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.ChatHandler;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;
import com.titankingdoms.nodinchan.titanchat.util.Debugger.DebugLevel;

public final class ChannelParticipant extends Participant {
	
	private final Debugger db = new Debugger(2, "ChannelParticipant");
	
	private final Map<String, Boolean> mutes = new HashMap<String, Boolean>();
	
	public ChannelParticipant(String name) {
		super(name);
	}
	
	public ChatHandler getChatHandler() {
		return null;
	}
	
	@Override
	public CommandSender getCommandSender() {
		return plugin.getServer().getPlayer(getName());
	}
	
	@Override
	public boolean hasPermission(String permission) {
		if (!isOnline())
			return false;
		
		return getCommandSender().hasPermission(permission);
	}
	
	@Override
	public boolean isMuted(String channel) {
		return (mutes.containsKey(channel.toLowerCase())) ? mutes.get(channel.toLowerCase()) : false;
	}
	
	@Override
	public boolean isOnline() {
		return plugin.getServer().getOfflinePlayer(getName()).isOnline();
	}
	
	@Override
	public void mute(String channel, boolean mute) {
		this.mutes.put(channel.toLowerCase(), mute);
	}
	
	@Override
	public void send(String... messages) {
		if (!isOnline())
			return;
		
		for (String message : messages)
			db.debug(DebugLevel.I, "@" + getName() + ": " + message);
		
		getCommandSender().sendMessage(messages);
	}
}