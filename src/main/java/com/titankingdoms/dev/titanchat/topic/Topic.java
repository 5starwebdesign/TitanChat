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

package com.titankingdoms.dev.titanchat.topic;

import com.titankingdoms.dev.titanchat.TitanChat;

/**
 * {@link Topic} - Information topic
 * 
 * @author NodinChan
 *
 */
public abstract class Topic {
	
	protected final TitanChat plugin;
	
	private final String name;
	private final String description;
	
	private String information;
	
	public Topic(String name, String description) {
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.description = description;
		this.information = "";
	}
	
	/**
	 * Gets the description of the {@link Topic}
	 * 
	 * @return The description
	 */
	public final String getDescription() {
		return description;
	}
	
	/**
	 * Gets the information of the {@link Topic}
	 * 
	 * @return The information
	 */
	public String getInformation() {
		return (information.isEmpty()) ? description : information;
	}
	
	/**
	 * Gets the name of the {@link Topic}
	 * 
	 * @return The name
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * Sets the information of the {@link Topic}
	 * 
	 * @param information The new information
	 */
	public void setInformation(String information) {
		this.information = (information != null) ? information : "";
	}
}