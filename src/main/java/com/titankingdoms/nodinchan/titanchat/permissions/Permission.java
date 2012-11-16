package com.titankingdoms.nodinchan.titanchat.permissions;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;

public enum Permission {
	AUTODIRECT("autodirect"),
	AUTOJOIN("autojoin"),
	AUTOLEAVE("autoleave"),
	BAN("ban"),
	EMOTE("emotes"),
	JOIN("join"),
	KICK("kick"),
	LEAVE("leave"),
	MUTE("mute"),
	RANK("rank"),
	SPAWN("spawn"),
	SPEAK("speak"),
	VOICE("voice");
	
	private String name;
	
	private Permission(String name) {
		this.name = name;
	}
	
	public String getPermission() {
		return "TitanChat." + name + ".*";
	}
	
	public String getPermission(Channel channel) {
		return "TitanChat." + name + "." + channel.getName();
	}
}