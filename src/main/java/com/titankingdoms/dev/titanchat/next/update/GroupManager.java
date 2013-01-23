package com.titankingdoms.dev.titanchat.next.update;

import java.util.HashMap;
import java.util.Map;

public final class GroupManager {
	
	private final Map<String, ChatGroup> groups;
	
	public GroupManager() {
		this.groups = new HashMap<String, ChatGroup>();
	}
	
	public ChatGroup getGroup(String name) {
		return groups.get(name.toLowerCase());
	}
	
	public boolean hasGroup(String name) {
		return groups.containsKey(name.toLowerCase());
	}
	
	public void load() {
		
	}
}