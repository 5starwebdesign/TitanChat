package com.titankingdoms.nodinchan.titanchat.core.channel;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;

import com.titankingdoms.nodinchan.titanchat.core.ChatTarget;
import com.titankingdoms.nodinchan.titanchat.core.addon.Addon;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;
import com.titankingdoms.nodinchan.titanchat.loading.Loadable;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;

public abstract class Channel extends Loadable implements ChatTarget, Listener {
	
	protected final Debugger db = new Debugger(2, "Channel");
	
	private final Type type;
	
	private final Set<String> admins;
	private final Set<String> blacklist;
	private final Set<String> participants;
	private final Set<String> whitelist;
	
	private final Map<String, Command> commands;
	
	public Channel(String name, Type type) {
		super(name);
		this.type = type;
		this.admins = new HashSet<String>();
		this.blacklist = new HashSet<String>();
		this.participants = new HashSet<String>();
		this.whitelist = new HashSet<String>();
		this.commands = new HashMap<String, Command>();
	}
	
	public void broadcast(String... messages) {
		for (String name : participants)
			plugin.getParticipant(name).send(messages);
	}
	
	public Set<String> getAdmins() {
		return admins;
	}
	
	public Set<String> getBlacklist() {
		return blacklist;
	}
	
	public abstract ChannelLoader getChannelLoader();
	
	public final Command getCommand(String name) {
		return this.commands.get(name.toLowerCase());
	}
	
	public abstract ChannelInfo getInfo();
	
	public Set<String> getParticipants() {
		return Collections.unmodifiableSet(participants);
	}
	
	public abstract Range getRange();
	
	public final Type getType() {
		return type;
	}
	
	public Set<String> getWhitelist() {
		return whitelist;
	}
	
	public final boolean hasCommand(String name) {
		return this.commands.containsKey(name.toLowerCase());
	}
	
	public boolean isAdmin(String name) {
		return admins.contains(name);
	}
	
	public boolean isAdmin(Participant participant) {
		return isAdmin(participant.getName());
	}
	
	public boolean isAdmin(OfflinePlayer player) {
		return isAdmin(player.getName());
	}
	
	public boolean isBlacklisted(String name) {
		return blacklist.contains(name);
	}
	
	public boolean isBlacklisted(Participant participant) {
		return isBlacklisted(participant.getName());
	}
	
	public boolean isBlacklisted(OfflinePlayer player) {
		return isBlacklisted(player.getName());
	}
	
	public boolean isParticipating(String name) {
		return participants.contains(name);
	}
	
	public boolean isParticipating(Participant participant) {
		return isParticipating(participant.getName());
	}
	
	public boolean isParticipating(OfflinePlayer player) {
		return isParticipating(player.getName());
	}
	
	public boolean isWhitelisted(String name) {
		return whitelist.contains(name);
	}
	
	public boolean isWhitelisted(Participant participant) {
		return isWhitelisted(participant.getName());
	}
	
	public boolean isWhitelisted(OfflinePlayer player) {
		return isWhitelisted(player.getName());
	}
	
	public void join(Participant participant) {
		if (participant == null)
			return;
		
		if (!participants.contains(participant.getName()))
			participants.add(participant.getName());
		
		if (!participant.isParticipating(this))
			participant.join(this);
	}
	
	public void leave(Participant participant) {
		if (participant == null)
			return;
		
		if (participants.contains(participant.getName()))
			participants.remove(participant.getName());
		
		if (participant.isParticipating(this))
			participant.leave(this);
	}
	
	public final void register(Addon... addons) {
		plugin.getAddonManager().register(addons);
	}
	
	public final void register(Command... commands) {
		plugin.getCommandManager().register(commands);
	}
	
	protected final void registerCommands(Command... commands) {
		for (Command command : commands)
			if (!hasCommand(command.getName()))
				this.commands.put(command.getName().toLowerCase(), command);
	}
	
	public abstract void reload();
}