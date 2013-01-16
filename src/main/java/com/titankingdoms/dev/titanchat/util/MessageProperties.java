package com.titankingdoms.dev.titanchat.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import com.titankingdoms.dev.titanchat.TitanChat;

public final class MessageProperties {
	
	private static File propertiesFile;
	private static final Properties properties = new Properties();
	
	private static final Map<String, String> defaults = new TreeMap<String, String>();
	
	public static String getMessage(String key) {
		return properties.getProperty(key, defaults.get(key));
	}
	
	public static String getMessage(String key, String def) {
		return properties.getProperty(key, def);
	}
	
	public static void reloadProperties() {
		TitanChat plugin = TitanChat.getInstance();
		
		if (propertiesFile == null)
			propertiesFile = new File(plugin.getDataFolder(), "message.properties");
		
		try { properties.load(new FileInputStream(propertiesFile)); } catch (Exception e) {}
		
		for (Entry<String, String> def : defaults.entrySet())
			if (!properties.containsKey(def.getKey()))
				properties.setProperty(def.getKey(), def.getValue());
	}
	
	public static void setMessage(String key, String message) {
		properties.setProperty(key, message);
	}
}