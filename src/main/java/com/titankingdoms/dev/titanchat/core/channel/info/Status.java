package com.titankingdoms.dev.titanchat.core.channel.info;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Status {
	DEFAULT("default"),
	NONE("none"),
	STAFF("staff");
	
	private String name;
	private static final Map<String, Status> NAME_MAP = new HashMap<String, Status>();
	
	private Status(String name) {
		this.name = name;
	}
	
	static {
		for (Status type : EnumSet.allOf(Status.class))
			NAME_MAP.put(type.name, type);
	}
	
	public static Status fromName(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}
	
	public String getName() {
		return name;
	}
}