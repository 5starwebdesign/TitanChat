package com.titankingdoms.nodinchan.titanchat.core.channel.utility;

import java.util.List;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelInfo;
import com.titankingdoms.nodinchan.titanchat.core.channel.Range;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;

public final class WhisperChannel extends UtilityChannel {
	
	public WhisperChannel() {
		super("Whisper");
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
	public List<Participant> selectRecipants(Player sender, String message) {
		return null;
	}
}