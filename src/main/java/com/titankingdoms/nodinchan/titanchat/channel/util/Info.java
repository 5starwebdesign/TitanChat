/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat.channel.util;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel.Range;

/**
 * Info - Info about channels
 * 
 * @author NodinChan
 *
 */
public class Info {
	
	protected final TitanChat plugin;
	
	private final Channel channel;
	
	public Info(Channel channel) {
		this.plugin = TitanChat.getInstance();
		this.channel = channel;
	}
	
	/**
	 * Whether the channel should colour chat
	 * 
	 * @return True if chat should be coloured
	 */
	public boolean colouring() {
		return channel.getConfig().getBoolean("setting.colouring", false);
	}
	
	/**
	 * Wheher the channel should send join messages
	 * 
	 * @return True if the join messages should be sent
	 */
	public boolean enableJoinMessage() {
		return channel.getConfig().getBoolean("messages.join", true);
	}
	
	/**
	 * Whether the channel should send leave messages
	 * 
	 * @return True if the leave messages should be sent
	 */
	public boolean enableLeaveMessage() {
		return channel.getConfig().getBoolean("messages.leave", true);
	}
	
	/**
	 * Gets the chat colour
	 * 
	 * @return The chat colour of the channel
	 */
	public String getChatColour() {
		return channel.getConfig().getString("chat-display-colour", "");
	}
	
	/**
	 * Gets the format
	 * 
	 * @return The format of the channel
	 */
	public String getFormat() {
		String format = channel.getConfig().getString("format", "");
		return (format.isEmpty()) ? plugin.getConfig().getString("formatting.format") : format;
	}
	
	/**
	 * Gets the tag
	 * 
	 * @return The tag of the channel
	 */
	public String getTag() {
		return channel.getConfig().getString("tag", "");
	}
	
	/**
	 * Gets the topic
	 * 
	 * @return The topic of the channel
	 */
	public String getTopic() {
		return channel.getConfig().getString("topic", "");
	}
	
	/**
	 * Gets the radius
	 * 
	 * @return The radius of the channel if range is Local
	 */
	public int radius() {
		return channel.getConfig().getInt("setting.radius", 0);
	}
	
	/**
	 * Gets the range
	 * 
	 * @return The range of the channel
	 */
	public Range range() {
		return Range.fromName(channel.getConfig().getString("setting.range", "channel"));
	}
	
	/**
	 * Sets the chat colour
	 * 
	 * @param colour The new chat colour
	 */
	public void setChatColour(String colour) {
		channel.getConfig().set("chat-display-colour", colour);
		channel.saveConfig();
	}
	
	/**
	 * Sets whether chat should be coloured
	 * 
	 * @param colouring Whether the chat should be coloured
	 */
	public void setColouring(boolean colouring) {
		channel.getConfig().set("setting.colouring", colouring);
		channel.saveConfig();
	}
	
	/**
	 * Sets the format
	 * 
	 * @param format The new format
	 */
	public void setFormat(String format) {
		channel.getConfig().set("format", format);
		channel.saveConfig();
	}
	
	/**
	 * Gets the radius
	 * 
	 * @param radius The new radius
	 */
	public void setRadius(int radius) {
		channel.getConfig().set("setting.radius", radius);
		channel.saveConfig();
	}
	
	/**
	 * Sets the range
	 * 
	 * @param range The new range
	 */
	public void setRange(Range range) {
		channel.getConfig().set("setting.range", range.getName());
		channel.saveConfig();
	}
	
	/**
	 * Sets the tag
	 * 
	 * @param tag The new tag
	 */
	public void setTag(String tag) {
		channel.getConfig().set("tag", tag);
		channel.saveConfig();
	}
	
	/**
	 * Sets the topic
	 * 
	 * @param topic The new topic
	 */
	public void setTopic(String topic) {
		channel.getConfig().set("topic", topic);
		channel.saveConfig();
	}
	
	/**
	 * Sets whether the channel is whitelist only
	 * 
	 * @param whitelistOnly Whether the channel is whitelist only
	 */
	public void setWhitelistOnly(boolean whitelistOnly) {
		channel.getConfig().set("setting.whitelist", whitelistOnly);
		channel.saveConfig();
	}
	
	/**
	 * Checks whether the channel is whitelist only
	 * 
	 * @return True if the channel is whitelist only
	 */
	public boolean whitelistOnly() {
		return channel.getConfig().getBoolean("setting.whitelist");
	}
}