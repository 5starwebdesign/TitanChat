package com.titankingdoms.dev.titanchat.core.channel.info;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Range {
	CHANNEL("channel"),
	GLOBAL("global"),
	LOCAL("local"),
	WORLD("world");
	
	private String name;
	private static final Map<String, Range> NAME_MAP = new HashMap<String, Range>();
	
	private Range(String name) {
		this.name = name;
	}
	
	static {
		for (Range range : EnumSet.allOf(Range.class))
			NAME_MAP.put(range.name, range);
	}
	
	public static Range fromName(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}
	
	public String getName() {
		return name;
	}
}