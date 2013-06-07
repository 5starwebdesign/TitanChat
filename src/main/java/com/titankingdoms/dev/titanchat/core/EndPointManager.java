package com.titankingdoms.dev.titanchat.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EndPointManager {
	
	private static final Map<String, EndPointSearch> search = new HashMap<String, EndPointSearch>();
	
	private static EndPointSearch getSearch(String pointType) {
		return search.get((pointType != null) ? pointType.toLowerCase() : "");
	}
	
	public static List<String> getSearchTypes() {
		List<String> list = new ArrayList<String>(search.keySet());
		Collections.sort(list);
		return list;
	}
	
	public static boolean hasSearch(String pointType) {
		return search.containsKey((pointType != null) ? pointType.toLowerCase() : "");
	}
	
	public static void registerSearch(EndPointSearch search) {
		if (search == null || hasSearch(search.getPointType()))
			return;
		
		EndPointManager.search.put(search.getPointType().toLowerCase(), search);
	}
	
	public static EndPoint search(String pointType, String name) {
		return (hasSearch(pointType)) ? getSearch(pointType).getEndPoint((name != null) ? name : "") : null;
	}
	
	public static void unregisterSearch(EndPointSearch search) {
		if (search == null || !hasSearch(search.getPointType()))
			return;
		
		EndPointManager.search.remove(search.getPointType().toLowerCase());
	}
	
	public static interface EndPointSearch {
		
		public EndPoint getEndPoint(String name);
		
		public List<EndPoint> getEndPoints();
		
		public boolean hasEndPoint(String name);
		
		public String getPointType();
	}
}