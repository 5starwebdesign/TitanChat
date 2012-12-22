/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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