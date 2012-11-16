package com.titankingdoms.nodinchan.titanchat.participant;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.permissions.Permission;

public final class ParticipantManager {
	
	private final TitanChat plugin;
	
	private Map<String, ChannelParticipant> participants;
	
	public ParticipantManager() {
		this.plugin = TitanChat.getInstance();
		this.participants = new HashMap<String, ChannelParticipant>();
	}
	
	public boolean existingParticipant(String name) {
		return participants.containsKey(name.toLowerCase());
	}
	
	public boolean existingParticipant(Player player) {
		return existingParticipant(player.getName());
	}
	
	public ChannelParticipant getParticipant(String name) {
		return participants.get(name.toLowerCase());
	}
	
	public ChannelParticipant getParticipant(Player player) {
		return getParticipant(player.getName());
	}
	
	public void loadParticipant(Player player) {
		if (!existingParticipant(player))
			participants.put(player.getName().toLowerCase(), new ChannelParticipant(player));
		
		ChannelParticipant participant = getParticipant(player);
		
		for (Channel channel : plugin.getChannelManager().getChannels()) {
			if (participant.hasPermission(Permission.AUTOJOIN.getPermission(channel)))
				channel.join(participant);
			
			if (participant.hasPermission(Permission.AUTOLEAVE.getPermission(channel)))
				channel.leave(participant);
			
			if (participant.hasPermission(Permission.AUTODIRECT.getPermission(channel)))
				participant.direct(channel);
		}
	}
}