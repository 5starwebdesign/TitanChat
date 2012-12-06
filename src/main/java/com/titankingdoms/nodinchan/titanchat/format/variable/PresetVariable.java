package com.titankingdoms.nodinchan.titanchat.format.variable;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;

public final class PresetVariable implements FormatVariable {
	
	private final String tag;
	private final String info;
	
	protected PresetVariable(String infoType, String info) {
		this.tag = "%" + infoType;
		this.info = info;
	}
	
	public String getFormatTag() {
		return tag;
	}
	
	public String getVariable(Participant sender, Channel channel) {
		return info;
	}
}