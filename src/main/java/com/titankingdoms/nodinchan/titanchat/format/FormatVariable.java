package com.titankingdoms.nodinchan.titanchat.format;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;

public interface FormatVariable {
	
	public String getFormatTag();
	
	public String getVariable(Participant sender, Channel channel);
}