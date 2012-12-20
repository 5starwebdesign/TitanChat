package com.titankingdoms.nodinchan.titanchat.core.channel.server;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChatHandler;
import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;

public final class ServerChatHandler extends ChatHandler {
	
	private final Channel channel;
	
	public ServerChatHandler(ServerChannel channel) {
		this.channel = channel;
	}
	
	@Override
	public String getFormat() {
		return channel.getFormat();
	}
	
	@Override
	public Set<Participant> getRecipients() {
		Set<Participant> recipients = new HashSet<Participant>();
		
		for (Player player : plugin.getServer().getOnlinePlayers())
			recipients.add(plugin.getParticipantManager().getParticipant(player.getName()));
		
		return recipients;
 	}
}