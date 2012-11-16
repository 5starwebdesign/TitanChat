package com.titankingdoms.nodinchan.titanchat.participant;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;
import com.titankingdoms.nodinchan.titanchat.util.Debugger.DebugLevel;

public final class ChannelParticipant extends Participant {
	
	private final TitanChat plugin;
	
	private final Debugger db = new Debugger(2, "ChannelParticipant");
	
	private final Map<String, Boolean> muted;
	
	public ChannelParticipant(Player player) {
		super(player.getName());
		this.plugin = TitanChat.getInstance();
		this.muted = new HashMap<String, Boolean>();
	}
	
	@Override
	public void chat(Channel channel, String message) {
		if (!isOnline())
			return;
		
		if (channel != null) {
			
		}
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return plugin.getServer().getOfflinePlayer(getName());
	}
	
	public Player getPlayer() {
		return plugin.getServer().getPlayer(getName());
	}
	
	@Override
	public boolean hasPermission(String permission) {
		if (!isOnline())
			return false;
		
		return getPlayer().hasPermission(permission);
	}
	
	public boolean isMutedOn(String channel) {
		return (muted.containsKey(channel.toLowerCase())) ? muted.get(channel.toLowerCase()) : false;
	}
	
	public boolean isMutedOn(Channel channel) {
		return (channel != null) ? isMutedOn(channel.getName()) : false;
	}
	
	public boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}
	
	public void mute(String channel, boolean muted) {
		this.muted.put(channel.toLowerCase(), muted);
	}
	
	@Override
	public void send(String... messages) {
		if (!isOnline())
			return;
		
		for (String message : messages)
			db.debug(DebugLevel.I, "@" + getName() + ": " + message);
		
		getPlayer().sendMessage(messages);
	}
}