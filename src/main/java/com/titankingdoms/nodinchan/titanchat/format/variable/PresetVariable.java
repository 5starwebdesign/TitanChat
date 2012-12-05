package com.titankingdoms.nodinchan.titanchat.format.variable;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;

public final class PresetVariable implements FormatVariable {
	
	private final String tag;
	private final String permission;
	private final String info;
	
	public PresetVariable(String infoType, String info) {
		this("", infoType, info);
	}
	
	public PresetVariable(String group, String infoType, String info) {
		this.tag = "%" + infoType;
		this.permission = (group != null && !group.isEmpty()) ? "TitanChat.preset." + group : "";
		this.info = info;
	}
	
	public String getFormatTag() {
		return tag;
	}
	
	public String getVariable(Participant sender, Channel channel) {
		return (permission.isEmpty() || sender.hasPermission(permission)) ? info : "";
	}
}