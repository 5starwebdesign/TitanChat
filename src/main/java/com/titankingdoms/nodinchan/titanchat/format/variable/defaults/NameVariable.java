package com.titankingdoms.nodinchan.titanchat.format.variable.defaults;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;
import com.titankingdoms.nodinchan.titanchat.format.variable.FormatVariable;

public final class NameVariable implements FormatVariable {
	
	public String getFormatTag() {
		return "%name";
	}
	
	public String getVariable(Participant sender, Channel channel) {
		return (sender != null) ? sender.getName() : "";
	}
}