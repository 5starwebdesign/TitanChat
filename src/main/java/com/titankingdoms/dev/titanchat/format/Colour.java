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

import org.bukkit.ChatColor;

/**
 * {@link Colour} -  Represents {@link ChatColor}
 * 
 * @author NodinChan
 *
 */
public enum Colour {
	AZURE(ChatColor.DARK_AQUA),
	BLACK(ChatColor.BLACK),
	BLUE(ChatColor.DARK_BLUE),
	BOLD(ChatColor.BOLD),
	CYAN(ChatColor.AQUA),
	GOLD(ChatColor.GOLD),
	GREEN(ChatColor.DARK_GREEN),
	GREY(ChatColor.DARK_GRAY),
	ITALIC(ChatColor.ITALIC),
	LIGHT_BLUE(ChatColor.BLUE),
	LIGHT_GREEN(ChatColor.GREEN),
	LIGHT_RED(ChatColor.RED),
	OBFUSCATE(ChatColor.MAGIC),
	PINK(ChatColor.LIGHT_PURPLE),
	PURPLE(ChatColor.DARK_PURPLE),
	RED(ChatColor.DARK_RED),
	RESET(ChatColor.RESET),
	SILVER(ChatColor.GRAY),
	STRIKETHROUGH(ChatColor.STRIKETHROUGH),
	UNDERLINE(ChatColor.UNDERLINE),
	WHITE(ChatColor.WHITE),
	YELLOW(ChatColor.YELLOW);
	
	private ChatColor colour;
	
	private Colour(ChatColor colour) {
		this.colour = colour;
	}
	
	/**
	 * Gets the {@link ChatColor} from the code
	 * 
	 * @param code The code
	 * 
	 * @return The {@link ChatColor} if found, otherwise null
	 */
	public static ChatColor getByChar(char code) {
		return ChatColor.getByChar(code);
	}
	
	/**
	 * Gets the {@link ChatColor} from the code
	 * 
	 * @param code The code
	 * 
	 * @return The {@link ChatColor} if found, otherwise null
	 */
	public static ChatColor getByChar(String code) {
		return ChatColor.getByChar(code);
	}
	
	/**
	 * Gets the {@link Character} value associated with this {@link Colour}
	 * 
	 * @return A {@link Character} value of this colour code
	 */
	public char getChar() {
		return colour.getChar();
	}
	
	/**
	 * Gets the {@link ChatColor} used last in the given input
	 * 
	 * @param input The input to retrieve the colours from
	 * 
	 * @return The {@link ChatColor} used last
	 */
	public String getLastColours(String input) {
		return ChatColor.getLastColors(input);
	}
	
	/**
	 * Checks if the code is a colour code
	 * 
	 * @return True if is a colour code
	 */
	public boolean isColour() {
		return colour.isColor();
	}
	
	/**
	 * Checks if the code is a format code
	 * 
	 * @return True if is a format code
	 */
	public boolean isFormat() {
		return colour.isFormat();
	}
	
	/**
	 * Strips the given input of all colour codes
	 * 
	 * @param input The input to strip of colour
	 * 
	 * @return The decolourised input
	 */
	public static String stripColour(String input) {
		return ChatColor.stripColor(input);
	}
	
	/**
	 * Gets the {@link ChatColor} represented by this {@link Colour}
	 * 
	 * @return The corresponding {@link ChatColor}
	 */
	public ChatColor toChatColor() {
		return colour;
	}
	
	@Override
	public String toString() {
		return colour.toString();
	}
	
	/**
	 * Translates the alternate colour code characters to the internal colour code character
	 * 
	 * @param altColourChar The alternate colour code character used
	 * 
	 * @param textToTranslate The text to translate
	 * 
	 * @return The translated text
	 */
	public static String translateAlternateColourCodes(char altColourChar, String textToTranslate) {
		return ChatColor.translateAlternateColorCodes(altColourChar, textToTranslate);
	}
}