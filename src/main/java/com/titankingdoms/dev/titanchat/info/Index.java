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

package com.titankingdoms.dev.titanchat.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * {@link Index} - Index of {@link Topic}s
 * 
 * @author NodinChan
 *
 */
public abstract class Index extends Topic {
	
	private final Map<String, Topic> topics;
	
	public Index(String name) {
		this(name, "The " + name + " Index", new TreeMap<String, Topic>());
	}
	
	public Index(String name, String description) {
		this(name, description, new TreeMap<String, Topic>());
	}
	
	public Index(String name, Map<String, Topic> topics) {
		this(name, "The " + name + " Index", topics);
	}
	
	public Index(String name, String description, Map<String, Topic> topics) {
		super(name, description);
		this.topics = topics;
	}
	
	/**
	 * Adds the {@link Topic} to the {@link Index}
	 * 
	 * @param topic The {@link Topic} to add
	 */
	public void addTopic(Topic topic) {
		if (topic != null)
			this.topics.put(topic.getName().toLowerCase(), topic);
	}
	
	@Override
	public String getInformation() {
		StringBuilder information = new StringBuilder();
		
		for (Topic topic : getTopics()) {
			if (information.length() > 0)
				information.append("\n");
			
			String name = topic.getName();
			String description = topic.getDescription();
			
			information.append(name + ((!description.isEmpty()) ? " - " + description : ""));
		}
		
		return information.toString();
	}
	
	/**
	 * Gets the specified {@link Topic}
	 * 
	 * @param name The name of the {@link Topic}
	 * 
	 * @return The {@link Topic} if found, otherwise null
	 */
	public Topic getTopic(String name) {
		return topics.get((name != null) ? name.toLowerCase() : "");
	}
	
	/**
	 * Gets all {@link Topic}s
	 * 
	 * @return All added {@link Topic}s
	 */
	public List<Topic> getTopics() {
		return new ArrayList<Topic>(topics.values());
	}
	
	/**
	 * Removes the {@link Topic} from the {@link Index}
	 * 
	 * @param topic The {@link Topic} to remove
	 */
	public void removeTopic(Topic topic) {
		if (topic != null)
			this.topics.remove(topic.getName().toLowerCase());
	}
	
	@Override
	public void setInformation(String information) {}
}