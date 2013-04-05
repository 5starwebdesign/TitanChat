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

import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;

public final class Format {
	
	private final static Pattern colour = Pattern.compile("(?i)(&)([a-fk-or0-9])");
	
	public static final String DEFAULT_FORMAT = "%prefix%player%suffix\u00A7f: %message";
	
	public static String colourise(String text) {
		return text.replaceAll(colour.toString(), "\u00A7$2");
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