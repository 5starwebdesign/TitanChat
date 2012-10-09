package com.titankingdoms.nodinchan.titanchat.participant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;
import com.titankingdoms.nodinchan.titanchat.util.Debugger.DebugLevel;

public final class Participant {
	
	private final TitanChat plugin;
	
	private final Debugger db = new Debugger(2, "Participant");
	
	private final String name;
	
	private Channel current;
	private final Map<String, Channel> channels;
	
	private final Map<String, Boolean> muted;
	
	public Participant(Player player) {
		this.plugin = TitanChat.getInstance();
		this.name = player.getName();
		this.channels = new HashMap<String, Channel>();
		this.muted = new HashMap<String, Boolean>();
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
	
	public void direct(Channel channel) {
		this.current = channel;
		
		if (!isParticipating(channel))
			join(channel);
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
	
	public boolean hasPermission(String permission) {
		if (!isOnline())
			return false;
		
		return plugin.getPermBridge().hasPermission(getPlayer(), permission);
	}
	
	public boolean isDirectedAt(String channel) {
		return (current != null) ? current.getName().equalsIgnoreCase(channel) : false;
	}
	
	public boolean isDirectedAt(Channel channel) {
		return (channel != null) ? isDirectedAt(channel.getName()) : current == null;
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
	
	public boolean isParticipating(String channel) {
		return channels.containsKey(channel.toLowerCase());
	}
	
	public boolean isParticipating(Channel channel) {
		return (channel != null) ? isParticipating(channel.getName()) : false;
	}
	
	public void join(Channel channel) {
		if (channel == null)
			return;
		
		this.channels.put(channel.getName().toLowerCase(), channel);
		
		if (!this.current.equals(channel))
			direct(channel);
		
		if (!channel.isParticipating(this))
			channel.join(this);
	}
	
	public void leave(Channel channel) {
		if (channel == null)
			return;
		
		this.channels.remove(channel.getName().toLowerCase());
		
		if (this.current.equals(channel))
			direct(getChannels().iterator().hasNext() ? getChannels().iterator().next() : null);
		
		if (channel.isParticipating(this))
			channel.leave(this);
	}
	
	public void mute(String channel, boolean muted) {
		this.muted.put(channel.toLowerCase(), muted);
	}
	
	public void send(MessageLevel level, String message) {
		if (!isOnline())
			return;
		
		db.debug(DebugLevel.I, "@" + name + ": " + message);
		String format = "[" + level.getColour() + "TitanChat" + ChatColor.WHITE + "] " + level.getColour() + "%msg";
		getPlayer().sendMessage(plugin.getFormatHandler().splitAndFormat(format, "%msg", message));
	}
}