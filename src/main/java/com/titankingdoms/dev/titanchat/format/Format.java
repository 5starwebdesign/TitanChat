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

/**
 * {@link Format} - Formatting util
 * 
 * @author NodinChan
 *
 */
public final class Format {
	
	private static final Pattern colour = Pattern.compile("(?i)(&)([a-fk-or0-9])");
	
	private static final String DEFAULT_FORMAT = "%prefix%display%suffix\u00A7f: %colour%message";
	
	/**
	 * Colourises the text
	 * 
	 * @param text The text to colourise
	 * 
	 * @return The colourised text
	 */
	public static String colourise(String text) {
		return text.replaceAll(colour.toString(), "\u00A7$2");
	}
	
	/**
	 * Decolourises the text
	 * 
	 * @param text The text to decolourise
	 * 
	 * @return The decolourised text
	 */
	public static String decolourise(String text) {
		return text.replaceAll(colour.toString(), "");
	}
	
	/**
	 * Gets the default format
	 * 
	 * @return The default format
	 */
	public static String getDefaultFormat() {
		return DEFAULT_FORMAT;
	}
	
	/**
	 * Gets the format
	 * 
	 * @return The format
	 */
	public static String getFormat() {
		FileConfiguration config = TitanChat.getInstance().getConfig();
		return colourise(config.getString("formatting.format", DEFAULT_FORMAT));
	}
}