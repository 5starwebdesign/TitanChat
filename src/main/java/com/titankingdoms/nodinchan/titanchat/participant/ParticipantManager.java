package com.titankingdoms.nodinchan.titanchat.participant;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;

public final class ParticipantManager {
	
	private final TitanChat plugin;
	
	private Map<String, Participant> participants;
	
	public ParticipantManager() {
		this.plugin = TitanChat.getInstance();
		this.participants = new HashMap<String, Participant>();
	}
	
	public boolean existingParticipant(String name) {
		return participants.containsKey(name.toLowerCase());
	}
	
	public boolean existingParticipant(Player player) {
		return existingParticipant(player.getName());
	}
	
	public Participant getParticipant(String name) {
		return participants.get(name.toLowerCase());
	}
	
	public Participant getParticipant(Player player) {
		return getParticipant(player.getName());
	}
	
	public void loadParticipant(Player player) {
		if (!existingParticipant(player))
			participants.put(player.getName().toLowerCase(), new Participant(player));
		
		Participant participant = getParticipant(player);
		
		for (Channel channel : plugin.getManager().getChannelManager().getChannels()) {
			
		}
	}
}