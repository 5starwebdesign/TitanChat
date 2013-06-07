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

import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;

/**
 * {@link Format} - Formatting util
 * 
 * @author NodinChan
 *
 */
public final class Format {
	
	private static final String CHAT_FORMAT = "%eptag%prefix%display%suffix\u00A7f: %colour%message";
	private static final String EMOTE_FORMAT = "* %prefix%display%suffix \u00A7f%message";
	
	/**
	 * Colourises the text
	 * 
	 * @param text The text to colourise
	 * 
	 * @return The colourised text
	 */
	public static String colourise(String text) {
		return Colour.translateAlternateColourCodes('&', text);
	}
	
	/**
	 * Colourises the texts
	 * 
	 * @param texts The texts to colourise
	 * 
	 * @return The colourised texts
	 */
	public static String[] colourise(String... texts) {
		String[] colourised = new String[texts.length];
		
		for (int text = 0; text < texts.length; text++)
			colourised[text] = colourise(texts[text]);
		
		return colourised;
	}
	
	/**
	 * Decolourises the text
	 * 
	 * @param text The text to decolourise
	 * 
	 * @return The decolourised text
	 */
	public static String decolourise(String text) {
		return Colour.stripColour(Colour.translateAlternateColourCodes('&', text));
	}
	
	/**
	 * Decolourises the texts
	 * 
	 * @param texts The texts to decolourise
	 * 
	 * @return The decolourised texts
	 */
	public static String[] decolourise(String... texts) {
		String[] decolourised = new String[texts.length];
		
		for (int text = 0; text < texts.length; text++)
			decolourised[text] = decolourise(texts[text]);
		
		return decolourised;
	}
	
	/**
	 * Gets the format
	 * 
	 * @return The format
	 */
	public static String getChatFormat() {
		FileConfiguration config = TitanChat.getInstance().getConfig();
		return colourise(config.getString("formatting.chat", CHAT_FORMAT));
	}
	
	/**
	 * Gets the default format
	 * 
	 * @return The default format
	 */
	public static String getDefaultChatFormat() {
		return CHAT_FORMAT;
	}
	
	public static String getDefaultEmoteFormat() {
		return EMOTE_FORMAT;
	}
	
	public static String getEmoteFormat() {
		FileConfiguration config = TitanChat.getInstance().getConfig();
		return colourise(config.getString("formatting.emote", EMOTE_FORMAT));
	}
}