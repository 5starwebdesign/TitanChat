package com.titankingdoms.nodinchan.titanchat.format;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

public final class FormatUtils {
	
	private final static Pattern colour = Pattern.compile("(&)([a-fk-or0-9])", Pattern.CASE_INSENSITIVE);
	
	public static String colourise(String text) {
		return text.replaceAll(FormatUtils.colour.toString(), "\u00A7$2");
	}
	
	public static String decolourise(String text) {
		return text.replaceAll(FormatUtils.colour.toString(), "");
	}
	
	public static String[] split(String line) {
		List<String> lines = new LinkedList<String>();
		
		while (line.length() >= 119) {
			int end = line.lastIndexOf(' ', 119);
			
			if (end == -1)
				end = 119;
			
			lines.add(line.substring(0, end));
			line = (ChatColor.getLastColors(lines.get(lines.size() - 1)) + line.substring(end)).trim();
		}
		
		if (line.length() > 0)
			lines.add(line);
		
		return lines.toArray(new String[0]);
	}
	
	public static String[] splitAndFormat(String format, String variable, String line) {
		return split(format.replace(variable, line));
	}
}