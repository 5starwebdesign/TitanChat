package com.titankingdoms.dev.titanchat.next.update;

import java.util.HashMap;
import java.util.Map;

public final class ChatEntityManager {
	
	private final Map<String, ChatEntitySearcher> searchers;
	
	public ChatEntityManager() {
		this.searchers = new HashMap<String, ChatEntitySearcher>();
	}
	
	public ChatEntity getEntity(String type, String name) {
		if (!searchers.containsKey(type.toLowerCase()))
			return null;
		
		return searchers.get(type.toLowerCase()).getEntity(name);
	}
	
	public void registerSearcher(ChatEntitySearcher searcher) {
		if (!searchers.containsKey(searcher.getEntityType().toLowerCase()))
			searchers.put(searcher.getEntityType().toLowerCase(), searcher);
	}
	
	public static abstract class ChatEntitySearcher {
		
		private final String type;
		
		public ChatEntitySearcher(String type) {
			this.type = type;
		}
		
		public abstract ChatEntity getEntity(String name);
		
		public final String getEntityType() {
			return type;
		}
	}
}