package com.titankingdoms.nodinchan.titanchat.channel.enumeration;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Access {
	BAN("ban"),
	DEMOTE("demote"),
	JOIN("join"),
	KICK("kick"),
	LEAVE("leave"),
	MUTE("mute"),
	PROMOTE("promote"),
	SPEAK("speak"),
	UNBAN("unban"),
	UNMUTE("unmute"),
	UNWHITELIST("unwhitelist"),
	WHITELIST("whitelist");
	
	private String name;
	private static Map<String, Access> NAME_MAP = new HashMap<String, Access>();
	
	private Access(String name) {
		this.name = name;
	}
	
	static {
		for (Access access : EnumSet.allOf(Access.class))
			NAME_MAP.put(access.name.toLowerCase(), access);
	}
	
	public static Access fromName(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}
	
	public String getName() {
		return name;
	}
}