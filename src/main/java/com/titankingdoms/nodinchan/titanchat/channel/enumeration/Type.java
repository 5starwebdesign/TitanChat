package com.titankingdoms.nodinchan.titanchat.channel.enumeration;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Type {
	CUSTOM("custom"),
	DEFAULT("default"),
	NONE("none"),
	STAFF("staff"),
	UTIL("util");
	
	private String name;
	private static Map<String, Type> NAME_MAP = new HashMap<String, Type>();
	
	private Type(String name) {
		this.name = name;
	}
	
	static {
		for (Type type : EnumSet.allOf(Type.class))
			NAME_MAP.put(type.name.toLowerCase(), type);
	}
	
	public static Type fromName(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}
	
	public String getName() {
		return name;
	}
}