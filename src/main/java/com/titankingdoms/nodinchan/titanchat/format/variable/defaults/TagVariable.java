package com.titankingdoms.nodinchan.titanchat.format.variable.defaults;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;
import com.titankingdoms.nodinchan.titanchat.format.variable.FormatVariable;

public final class TagVariable implements FormatVariable {
	
	public String getFormatTag() {
		return "%tag";
	}
	
	public String getVariable(Participant sender, Channel channel) {
		if (channel == null || channel.getInfo() == null)
			return "";
		
		return (channel.getInfo().getTag() != null) ? channel.getInfo().getTag() : "";
	}
}