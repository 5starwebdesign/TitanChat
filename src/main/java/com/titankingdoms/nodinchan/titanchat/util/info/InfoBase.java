package com.titankingdoms.nodinchan.titanchat.util.info;

import java.util.HashMap;
import java.util.Map;

public class InfoBase {
	
	private final Map<String, String> infoMap;
	
	public InfoBase() {
		this.infoMap = new HashMap<String, String>();
	}
	
	public Map<String, String> getAllInfo() {
		return new HashMap<String, String>(infoMap);
	}
	
	public String getInfo(String infoType) {
		return infoMap.get(infoType.toLowerCase());
	}
	
	public boolean hasInfo(String infoType) {
		return infoMap.containsKey(infoType.toLowerCase());
	}
	
	public void setInfo(String infoType, String newInfo) {
		infoMap.put(infoType.toLowerCase(), newInfo);
	}
}