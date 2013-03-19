package com.titankingdoms.dev.titanchat.format;

import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;

public final class Format {
	
	private final static Pattern colour = Pattern.compile("(?i)(&)([a-fk-or0-9])");
	
	public static final String DEFAULT_FORMAT = "%prefix%player%suffix§f: %message";
	
	public static String colourise(String text) {
		return text.replaceAll(colour.toString(), "§$2");
	}
	
	public static String decolourise(String text) {
		return text.replaceAll(colour.toString(), "");
	}
	
	public static String getDefaultFormat() {
		return DEFAULT_FORMAT;
	}
	
	public static String getFormat() {
		FileConfiguration config = TitanChat.getInstance().getConfig();
		return colourise(config.getString("formatting.format", DEFAULT_FORMAT));
	}
}