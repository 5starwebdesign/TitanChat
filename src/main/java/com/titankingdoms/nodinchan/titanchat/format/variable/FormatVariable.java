package com.titankingdoms.nodinchan.titanchat.format.variable;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;

public interface FormatVariable {
	
	public String getFormatTag();
	
	public String getVariable(Participant sender, Channel channel);
}