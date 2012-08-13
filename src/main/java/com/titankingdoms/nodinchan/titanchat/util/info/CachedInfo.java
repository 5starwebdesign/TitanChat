package com.titankingdoms.nodinchan.titanchat.util.info;

import java.util.HashMap;
import java.util.Map;

public final class CachedInfo {
	
	private final String name;
	
	private final Map<String, String> infoMap;
	
	public CachedInfo(String name) {
		this.name = name;
		this.infoMap = new HashMap<String, String>();
	}
	
	public String getInfo(String infoType) {
		return infoMap.get(infoType);
	}
	
	public String getName() {
		return name;
	}
	
	public void setInfo(String infoType, String newInfo) {
		infoMap.put(infoType, newInfo);
	}
}