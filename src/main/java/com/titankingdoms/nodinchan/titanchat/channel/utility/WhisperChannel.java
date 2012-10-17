package com.titankingdoms.nodinchan.titanchat.channel.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.channel.ChannelInfo;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Access;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Range;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;

public final class WhisperChannel extends UtilityChannel {
	
	private final Map<String, Participant> targets;
	
	public WhisperChannel() {
		super("Whisper");
		this.targets = new HashMap<String, Participant>();
	}
	
	@Override
	public ChannelInfo getInfo() {
		return null;
	}
	
	@Override
	public Range getRange() {
		return Range.CHANNEL;
	}
	
	@Override
	public boolean hasAccess(Player player, Access access) {
		return false;
	}
	
	@Override
	public List<Player> selectRecipants(Player sender, String message) {
		return null;
	}
	
	public void target(Player sender, Participant target) {
		
	}
}