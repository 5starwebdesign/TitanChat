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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.format.tag.defaults.*;
import com.titankingdoms.dev.titanchat.util.Debugger;

/**
 * {@link TagManager} - Manages {@link Tag}s
 * 
 * @author NodinChan
 *
 */
public class TagManager {
	
	private final TitanChat plugin;
	
	private final Debugger db = new Debugger(5, "TagManager");
	
	private final Map<String, Tag> tags;
	
	public TagManager() {
		this.plugin = TitanChat.getInstance();
		this.tags = new HashMap<String, Tag>();
	}
	
	/**
	 * Gets the default format for variables
	 * 
	 * @return The default format for variables
	 */
	public String getDefaultTagFormat() {
		return plugin.getConfig().getString("formatting.tag-format.default", "%var%");
	}
	
	/**
	 * Gets the specified {@link Tag}
	 * 
	 * @param tag The name of the {@link Tag}
	 * 
	 * @return The specified {@link Tag} if found, otherwise null
	 */
	public Tag getTag(String tag) {
		return tags.get(tag.toLowerCase());
	}
	
	/**
	 * Checks if the {@link Tag} is registered
	 * 
	 * @param tag The name of the {@link Tag}
	 * 
	 * @return True if found
	 */
	public boolean hasTag(String tag) {
		return tags.containsKey((tag != null) ? tag.toLowerCase() : "");
	}
	
	/**
	 * Checks if the {@link Tag} is registered
	 * 
	 * @param tag The {@link Tag}
	 * 
	 * @return True if found
	 */
	public boolean hasTag(Tag tag) {
		return hasTag((tag != null) ? tag.getTag() : "");
	}
	
	/**
	 * Loads the manager
	 */
	public void load() {
		registerTags(
				new ColourTag(),
				new DisplayNameTag(),
				new NameTag(),
				new PrefixTag(),
				new SuffixTag()
		);
	}
	
	/**
	 * Registers the {@link Tag}s
	 * 
	 * @param tags The {@link Tag}s to register
	 */
	public void registerTags(Tag... tags) {
		if (tags == null)
			return;
		
		for (Tag tag : tags) {
			if (tag == null)
				continue;
			
			if (hasTag(tag)) {
				plugin.log(Level.WARNING, "Duplicate tag: " + tag.getTag());
				continue;
			}
			
			this.tags.put(tag.getTag().toLowerCase(), tag);
			db.debug(Level.INFO, "Registered tag: " + tag.getTag());
		}
	}
	
	/**
	 * Unloads the manager
	 */
	public void unload() {
		this.tags.clear();
	}
	
	/**
	 * Unregisters the {@link Tag}
	 * 
	 * @param tag The {@link Tag} to unregister
	 */
	public void unregisterTag(Tag tag) {
		if (tag == null || !hasTag(tag))
			return;
		
		this.tags.remove(tag.getTag().toLowerCase());
		db.debug(Level.INFO, "Unregistered tag: " + tag.getTag());
	}
}