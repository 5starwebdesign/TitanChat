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

package com.titankingdoms.dev.titanchat.format.tag;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

/**
 * {@link Tag} - Tag for formatting
 * 
 * @author NodinChan
 *
 */
public abstract class Tag {
	
	private final TitanChat plugin;
	
	private final String tag;
	
	public Tag(String tag) {
		this.plugin = TitanChat.getInstance();
		this.tag = tag;
	}
	
	/**
	 * Gets the format for the variable
	 * 
	 * @return The variable format
	 */
	public String getFormat() {
		String format = plugin.getConfig().getString("formatting.tag-format.tags." + tag.substring(1), "%tag%");
		return (format != null && !format.isEmpty()) ? format : plugin.getTagManager().getDefaultTagFormat();
	}
	
	/**
	 * Gets the tag
	 * 
	 * @return The tag
	 */
	public final String getTag() {
		return tag;
	}
	
	/**
	 * Gets the variable represented by the tag
	 * 
	 * @param participant The {@link Participant} chatting
	 * 
	 * @param channel The {@link Channel} being chat in
	 * 
	 * @return The variable
	 */
	public abstract String getVariable(Participant participant, Channel channel);
}