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

package com.titankingdoms.dev.titanchat.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.titankingdoms.dev.titanchat.util.loading.Loadable;

/**
 * {@link ChatEntity} - Represents a data holding entity
 * 
 * @author NodinChan
 *
 */
public abstract class ChatEntity extends Loadable {
	
	private final String type;
	
	private final Map<String, Data> data;
	
	public ChatEntity(String type, String name) {
		super(name);
		this.type = type;
		this.data = new HashMap<String, Data>();
	}
	
	/**
	 * Gets the specified {@link Data} from cache
	 * 
	 * @param key The key of the data pair
	 * 
	 * @param def The default value
	 * 
	 * @return The {@link Data} if found, otherwise the default value
	 */
	public final Data getData(String key, Data def) {
		return (data.containsKey(key)) ? data.get(key) : def;
	}
	
	/**
	 * Gets the specified {@link Data} from cache
	 * 
	 * @param key The key of the data pair
	 * 
	 * @param def The default value
	 * 
	 * @return The {@link Data} if found, otherwise the default value
	 */
	public final Data getData(String key, Object def) {
		return getData(key, new Data(def));
	}
	
	/**
	 * Gets the specified {@link Data} from cache
	 * 
	 * @param key The key of the data pair
	 * 
	 * @return The {@link Data} if found, otherwise null
	 */
	public final Data getData(String key) {
		return getData(key, null);
	}
	
	/**
	 * Gets the cached data map
	 * 
	 * @return The cached data map
	 */
	public final Map<String, Data> getDataMap() {
		return new HashMap<String, Data>(data);
	}
	
	/**
	 * Gets the {@link ConfigurationSection} that stores the {@link Data}
	 * 
	 * @return The {@link ConfigurationSection} that stores the {@link Data}
	 */
	public abstract ConfigurationSection getDataSection();
	
	/**
	 * The type of ChatEntity
	 * 
	 * @return The type
	 */
	public final String getEntityType() {
		return type;
	}
	
	/**
	 * Initialises the ChatEntity
	 */
	public void init() {
		loadData();
	}
	
	/**
	 * Loads the {@link Data} from the data section
	 */
	public final void loadData() {
		ConfigurationSection dataSection = getDataSection();
		
		if (dataSection == null)
			return;
		
		this.data.clear();
		
		for (String key : dataSection.getKeys(false))
			setData(key, dataSection.get(key));
	}
	
	/**
	 * Removes the {@link Data} from cache
	 * 
	 * @param key The key of the data pair
	 */
	public final void removeData(String key) {
		this.data.remove(key);
	}
	
	/**
	 * Saves the ChatEntity
	 */
	public void save() {
		saveData();
	}
	
	/**
	 * Saves the {@link Data} to the data section
	 */
	public final void saveData() {
		ConfigurationSection dataSection = getDataSection();
		
		if (dataSection == null)
			return;
		
		for (String key : new HashSet<String>(dataSection.getKeys(false)))
			dataSection.set(key, null);
		
		for (String key : data.keySet())
			dataSection.set(key, data.get(key).asString());
	}
	
	/**
	 * Sends the messages to the ChatEntity
	 * 
	 * @param message The messages
	 */
	public abstract void sendMessage(String... messages);
	
	/**
	 * Sets the {@link Data} in the cache
	 * 
	 * @param key The key of the data pair
	 * 
	 * @param value The new value
	 */
	public final void setData(String key, Object value) {
		setData(key, new Data(value));
	}
	
	/**
	 * Sets the {@link Data} in the cache
	 * 
	 * @param key The key of the data pair
	 * 
	 * @param value The new value
	 */
	public final void setData(String key, Data value) {
		this.data.put(key, value);
	}
}