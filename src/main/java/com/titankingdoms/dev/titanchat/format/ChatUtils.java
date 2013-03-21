/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat.format;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;

public final class ChatUtils {
	
	public static String[] paginate(String paragraph, int pageNumber, int lineLength, int pageHeight) {
		String[] lines = wordWrap(paragraph, lineLength);
		
		int totalPages = lines.length / pageHeight + ((lines.length % pageHeight != 0) ? 1 : 0);
		int page = (pageNumber > totalPages) ? totalPages : pageNumber;
		
		int from = (page - 1) * pageHeight;
		int to = ((from + pageHeight) > lines.length) ? lines.length : from + pageHeight;
		
		return Arrays.copyOfRange(lines, from, to);
	}
	
	public static String[][] paginate(String paragraph, int lineLength, int pageHeight) {
		List<String[]> pages = new LinkedList<String[]>();
		
		String[] lines = wordWrap(paragraph, lineLength);
		
		int totalPages = lines.length / pageHeight + ((lines.length % pageHeight != 0) ? 1 : 0);
		
		for (int page = 1; page <= totalPages; page++) {
			int from = (page - 1) * pageHeight;
			int to = ((from + pageHeight) > lines.length) ? lines.length : from + pageHeight;
			
			pages.add(Arrays.copyOfRange(lines, from, to));
		}
		
		return pages.toArray(new String[0][0]);
	}
	
	public static String[] wordWrap(String line, int lineLength) {
		List<String> lines = new LinkedList<String>();
		
		if (line.isEmpty())
			lines.add("");
		
		while (line.length() >= lineLength) {
			int end = line.lastIndexOf(' ', lineLength);
			
			if (end == -1)
				end = lineLength;
			
			lines.add(line.substring(0, end));
			line = (ChatColor.getLastColors(lines.get(lines.size() - 1)) + line.substring(end)).trim();
		}
		
		if (line.length() > 0)
			lines.add(line);
		
		return lines.toArray(new String[0]);
	}
}