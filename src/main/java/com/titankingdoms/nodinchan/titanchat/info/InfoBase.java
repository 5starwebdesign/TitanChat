package com.titankingdoms.nodinchan.titanchat.info;

import java.util.HashMap;
import java.util.Map;

public class InfoBase {
	
	private final Map<String, String> infoMap;
	
	public InfoBase() {
		this.infoMap = new HashMap<String, String>();
	}
	
	/**
	 * Gets all the info
	 * 
	 * @return All the info
	 */
	public Map<String, String> getAllInfo() {
		return new HashMap<String, String>(infoMap);
	}
	
	/**
	 * Gets the info by info type
	 * 
	 * @param infoType The info type
	 * 
	 * @return The info if found, otherwise null
	 */
	public String getInfo(String infoType) {
		return infoMap.get(infoType.toLowerCase());
	}
	
	/**
	 * Checks if an info exists for the specified info type
	 * 
	 * @param infoType The info type to check for
	 * 
	 * @return True if an info exists for the info type
	 */
	public boolean hasInfo(String infoType) {
		return infoMap.containsKey(infoType.toLowerCase());
	}
	
	/**
	 * Sets the info at the info type
	 * 
	 * @param infoType The info type to set for
	 * 
	 * @param newInfo The new info
	 */
	public void setInfo(String infoType, String newInfo) {
		infoMap.put(infoType.toLowerCase(), newInfo);
	}
}