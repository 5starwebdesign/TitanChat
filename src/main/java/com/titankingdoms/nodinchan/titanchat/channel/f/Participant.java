package com.titankingdoms.nodinchan.titanchat.channel.f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;
import com.titankingdoms.nodinchan.titanchat.util.Debugger.DebugLevel;

public final class Participant {
	
	private final TitanChat plugin;
	
	private final Debugger db = new Debugger(2, "Participant");
	
	private final String name;
	
	private Channel current;
	private final Map<String, Channel> channels;
	
	public Participant(String name) {
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.channels = new HashMap<String, Channel>();
	}
	
	public void chat(Channel channel, String message) {
		if (!isOnline())
			return;
		
		if (channel != null) {
			
		}
	}
	
	public void chat(String message) {
		chat(current, message);
	}
	
	public Set<Channel> getChannels() {
		return new HashSet<Channel>(channels.values());
	}
	
	public Channel getCurrentChannel() {
		return current;
	}
	
	public String getName() {
		return name;
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return plugin.getServer().getOfflinePlayer(name);
	}
	
	public Player getPlayer() {
		return plugin.getServer().getPlayer(name);
	}
	
	public boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}
	
	public boolean isParticipating(String channel) {
		return channels.containsKey(channel.toLowerCase());
	}
	
	public boolean isParticipating(Channel channel) {
		return isParticipating(channel.getName());
	}
	
	public void join(Channel channel) {
		if (channel == null)
			return;
		
		this.channels.put(channel.getName().toLowerCase(), channel);
		
		if (!this.current.equals(channel))
			this.current = channel;
		
		if (!channel.isParticipating(this))
			channel.join(this);
	}
	
	public void leave(Channel channel) {
		if (channel == null)
			return;
		
		this.channels.remove(channel.getName().toLowerCase());
		
		if (this.current.equals(channel))
			this.current = ((this.channels.size() > 0) ? getChannels().iterator().next() : null);
		
		if (channel.isParticipating(this))
			channel.leave(this);
	}
	
	public void send(MessageLevel level, String message) {
		if (!isOnline())
			return;
		
		db.debug(DebugLevel.I, "@" + name + ": " + message);
		String format = "[" + level.getColour() + "TitanChat" + ChatColor.WHITE + "] " + level.getColour() + "%msg";
		getPlayer().sendMessage(plugin.getFormatHandler().splitAndFormat(format, "%msg", message));
	}
}