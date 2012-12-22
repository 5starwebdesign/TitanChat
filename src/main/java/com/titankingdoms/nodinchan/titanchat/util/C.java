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

package com.titankingdoms.nodinchan.titanchat.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

public enum C {
	AQUA(ChatColor.AQUA),
	BLACK(ChatColor.BLACK),
	BLUE(ChatColor.BLUE),
	BOLD(ChatColor.BOLD),
	DARK_AQUA(ChatColor.DARK_AQUA),
	DARK_BLUE(ChatColor.DARK_BLUE),
	DARK_GREY(ChatColor.DARK_GRAY),
	DARK_GREEN(ChatColor.DARK_GREEN),
	DARK_PURPLE(ChatColor.DARK_PURPLE),
	DARK_RED(ChatColor.DARK_RED),
	GOLD(ChatColor.GOLD),
	GREY(ChatColor.GRAY),
	GREEN(ChatColor.GREEN),
	ITALIC(ChatColor.ITALIC),
	LIGHT_PURPLE(ChatColor.LIGHT_PURPLE),
	MAGIC(ChatColor.MAGIC),
	RED(ChatColor.RED),
	RESET(ChatColor.RESET),
	STRIKETHROUGH(ChatColor.STRIKETHROUGH),
	UNDERLINE(ChatColor.UNDERLINE),
	WHITE(ChatColor.WHITE),
	YELLOW(ChatColor.YELLOW);
	
	public static char COLOUR_CHAR = ChatColor.COLOR_CHAR;
	
	private ChatColor colour;
	private static Map<ChatColor, C> COLOUR_MAP = new HashMap<ChatColor, C>();
	
	private C(ChatColor colour) {
		this.colour = colour;
	}
	
	static {
		for (C colour : EnumSet.allOf(C.class))
			COLOUR_MAP.put(colour.toChatColor(), colour);
	}
	
	public static C fromChatColor(ChatColor colour) {
		return COLOUR_MAP.get(colour);
	}
	
	public static C getByChar(char code) {
		return fromChatColor(ChatColor.getByChar(code));
	}
	
	public static C getByChar(String code) {
		return fromChatColor(ChatColor.getByChar(code));
	}
	
	public char getChar() {
		return colour.getChar();
	}
	
	public static String getLastColours(String input) {
		return ChatColor.getLastColors(input);
	}
	
	public boolean isColour() {
		return colour.isColor();
	}
	
	public boolean isFormat() {
		return colour.isFormat();
	}
	
	public static String stripColour(String input) {
		return ChatColor.stripColor(input);
	}
	
	public ChatColor toChatColor() {
		return colour;
	}
	
	public static String translateAlternateColourCodes(char code, String input) {
		return ChatColor.translateAlternateColorCodes(code, input);
	}
	
	@Override
	public String toString() {
		return colour.toString();
	}
}