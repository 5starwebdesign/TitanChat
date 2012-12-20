package com.titankingdoms.nodinchan.titanchat.core.channel.setting;

import java.util.Map;
import java.util.TreeMap;

public final class SettingModifier {
	
	private final Map<String, Setting> settings;
	
	public SettingModifier() {
		this.settings = new TreeMap<String, Setting>();
	}
	
	public boolean existingSetting(String name) {
		return settings.containsKey(name.toLowerCase());
	}
	
	private boolean existingSetting(Setting setting) {
		return existingSetting(setting.getName());
	}
	
	public Setting getSetting(String name) {
		return settings.get(name.toLowerCase());
	}
	
	public void registerSettings(Setting... settings) {
		for (Setting setting : settings) {
			if (existingSetting(setting))
				continue;
			
			this.settings.put(setting.getName().toLowerCase(), setting);
		}
	}
}