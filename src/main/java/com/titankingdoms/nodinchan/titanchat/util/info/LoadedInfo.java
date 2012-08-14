package com.titankingdoms.nodinchan.titanchat.util.info;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

public final class LoadedInfo {
	
	private final InfoHandler handler;
	
	private final String path;
	
	private final String permission;
	
	private final Map<String, String> infoMap;
	
	public LoadedInfo(InfoHandler handler, String path, String permission) {
		this.handler = handler;
		this.path = path;
		this.permission = permission;
		this.infoMap = new HashMap<String, String>();
	}
	
	public String getInfo(String infoType) {
		return infoMap.get(infoType.toLowerCase());
	}
	
	public String getPath() {
		return path;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public ConfigurationSection getSection() {
		return handler.getSection(path);
	}
	
	public void setInfo(String infoType, String newInfo) {
		infoMap.put(infoType.toLowerCase(), newInfo);
	}
}