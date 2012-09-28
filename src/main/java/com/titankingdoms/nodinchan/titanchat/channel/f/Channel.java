package com.titankingdoms.nodinchan.titanchat.channel.f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.titankingdoms.nodinchan.titanchat.loading.Loadable;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;

public abstract class Channel extends Loadable implements Listener {
	
	protected final Debugger db = new Debugger(2, "Channel");
	
	private final List<String> admins;
	private final List<String> blacklist;
	private final List<String> followers;
	private final List<String> whitelist;
	
	private final Map<String, Participant> participants;
	
	public Channel(String name) {
		super(name);
		this.admins = new ArrayList<String>();
		this.blacklist = new ArrayList<String>();
		this.followers = new ArrayList<String>();
		this.whitelist = new ArrayList<String>();
		this.participants = new HashMap<String, Participant>();
	}
	
	public final List<String> getAdmins() {
		return admins;
	}
	
	public final List<String> getBlacklist() {
		return blacklist;
	}
	
	public abstract ChannelLoader getChannelLoader();
	
	public final List<String> getFollowers() {
		return followers;
	}
	
	public final List<Participant> getParticipants() {
		return new ArrayList<Participant>(participants.values());
	}
	
	public abstract RecipantSelector getRecipantSelector();
	
	public final List<String> getWhitelist() {
		return whitelist;
	}
	
	public final boolean isAdmin(Participant participant) {
		return admins.contains(participant.getName());
	}
	
	public final boolean isAdmin(OfflinePlayer player) {
		return admins.contains(player.getName());
	}
	
	public final boolean isBlacklisted(Participant participant) {
		return blacklist.contains(participant.getName());
	}
	
	public final boolean isBlacklisted(OfflinePlayer player) {
		return blacklist.contains(player.getName());
	}
	
	public final boolean isFollower(Participant participant) {
		return followers.contains(participant.getName());
	}
	
	public final boolean isFollower(OfflinePlayer player) {
		return followers.contains(player.getName());
	}
	
	public final boolean isParticipating(Participant participant) {
		return participants.containsKey(participant.getName().toLowerCase());
	}
	
	public final boolean isParticipating(OfflinePlayer player) {
		return participants.containsKey(player.getName().toLowerCase());
	}
	
	public final boolean isWhitelisted(Participant participant) {
		return whitelist.contains(participant.getName());
	}
	
	public final boolean isWhitelisted(OfflinePlayer player) {
		return whitelist.contains(player.getName());
	}
	
	public void join(Participant participant) {
		if (participant == null)
			return;
		
		if (!participants.containsKey(participant.getName().toLowerCase()))
			participants.put(participant.getName().toLowerCase(), participant);
		
		if (!participant.isParticipating(this))
			participant.join(this);
	}
	
	public void leave(Participant participant) {
		if (participant == null)
			return;
		
		if (participants.containsKey(participant.getName().toLowerCase()))
			participants.remove(participant.getName().toLowerCase());
		
		if (participant.isParticipating(this))
			participant.leave(this);
	}
	
	protected final String sendMessage(Player sender, List<Player> recipants, String message) {
		return sendMessage(sender, recipants.toArray(new Player[0]), message);
	}
	
	protected final String sendMessage(Player sender, Player[] recipants, String message) {
		return "";
	}
}