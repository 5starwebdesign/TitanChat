/*
 *     Copyright (C) 2013  Nodin Chan
 *     
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.titankingdoms.dev.titanchat.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

public final class FormatUtils {
	
	public static String censor(String text, Set<String> objectionable, String censor) {
		if (text == null)
			return "";
		
		if (objectionable == null || objectionable.isEmpty())
			return text;
		
		if (censor == null)
			censor = "";
		
		StringBuffer censored = new StringBuffer();
		
		Pattern censorship = Pattern.compile("(?i)(" + StringUtils.join(objectionable, '|') + ")");
		Matcher censoring = censorship.matcher(text);
		
		while (censoring.find())
			censoring.appendReplacement(censored, censoring.group().replaceAll(".", censor));
		
		return censoring.appendTail(censored).toString();
	}
	
	public static String colourise(String text, char colourChar) {
		return (text != null) ? ChatColor.translateAlternateColorCodes(colourChar, text) : text;
	}
	
	public static String decolourise(String text, char colourChar) {
		return ChatColor.stripColor(colourise(text, colourChar));
	}
	
	public static String[] paginate(String text, int page, int width, int height) {
		if (text == null || text.isEmpty() || width < 1)
			return new String[0];
		
		String[] lines = wrap(text, width);
		
		int maxPage = lines.length / height;
		
		if (lines.length % height != 0)
			maxPage++;
		
		if (page > maxPage)
			page = maxPage;
		
		int from = (page - 1) * height;
		int to = from + height;
		
		if (to > lines.length)
			to = lines.length;
		
		return Arrays.copyOfRange(lines, from, to);
	}
	
	public static String[] wrap(String text, int length) {
		if (text == null || text.isEmpty() || length < 1)
			return new String[0];
		
		if (text.length() == length)
			return new String[] { text };
		
		List<String> lines = new LinkedList<String>();
		
		while (text.length() >= length) {
			int end = text.lastIndexOf(' ', length);
			
			if (end < 0)
				end = length;
			
			int newLine = text.indexOf('\n');
			
			if (newLine > 0 && newLine < end)
				end = newLine;
			
			String line = text.substring(0, end + 1).trim();
			
			lines.add(line);
			
			text = ChatColor.getLastColors(line) + text.substring(end).trim();
		}
		
		if (text.length() > 0)
			lines.add(text);
		
		return lines.toArray(new String[0]);
	}
}