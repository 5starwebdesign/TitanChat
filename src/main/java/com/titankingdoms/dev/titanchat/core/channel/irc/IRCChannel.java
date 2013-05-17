package com.titankingdoms.dev.titanchat.core.channel.irc;

import java.util.Set;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.info.Range;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.format.Format;

public abstract class IRCChannel extends Channel {
	
	public IRCChannel(String name, String type, Status status) {
		super(name, type, status);
	}
	
	@Override
	public String[] getAliases() {
		if (getConfig().get("aliases", null) == null)
			return new String[0];
		
		return getConfig().getStringList("aliases").toArray(new String[0]);
	}
	
	@Override
	public String getDisplayColour() {
		return getConfig().getString("display-colour", "");
	}
	
	@Override
	public String getFormat() {
		return getConfig().getString("format", Format.getFormat());
	}
	
	public abstract IRCParticipant getIRCParticipant();
	
	public Set<Participant> getParticipants() {
		if (!isParticipating(getIRCParticipant()))
			join(getIRCParticipant());
		
		return super.getParticipants();
	}
	
	@Override
	public Range getRange() {
		return Range.fromName(getConfig().getString("range", "channel"));
	}
	
	@Override
	public String getTag() {
		return getConfig().getString("tag", "");
	}
}