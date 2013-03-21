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

import com.titankingdoms.dev.titanchat.data.Data;
import com.titankingdoms.dev.titanchat.loading.Loadable;

public abstract class ChatEntity extends Loadable {
	
	private final String type;
	
	private final Map<String, Data> data;
	
	public ChatEntity(String type, String name) {
		super(name);
		this.type = type;
		this.data = new HashMap<String, Data>();
	}
	
	public final Data getData(String key, Data def) {
		return (data.containsKey(key)) ? data.get(key) : def;
	}
	
	public final Data getData(String key, Object def) {
		return getData(key, new Data(def));
	}
	
	public final Data getData(String key) {
		return getData(key, null);
	}
	
	public final Map<String, Data> getDataMap() {
		return new HashMap<String, Data>(data);
	}
	
	public abstract ConfigurationSection getDataSection();
	
	public final String getEntityType() {
		return type;
	}
	
	public void init() {}
	
	public final void loadData() {
		ConfigurationSection dataSection = getDataSection();
		
		if (dataSection == null)
			return;
		
		this.data.clear();
		
		for (String key : dataSection.getKeys(false))
			setData(key, dataSection.get(key));
	}
	
	public final void removeData(String key) {
		this.data.remove(key);
	}
	
	public void save() {}
	
	public final void saveData() {
		ConfigurationSection dataSection = getDataSection();
		
		if (dataSection == null)
			return;
		
		for (String key : new HashSet<String>(dataSection.getKeys(false)))
			dataSection.set(key, null);
		
		for (String key : data.keySet())
			dataSection.set(key, data.get(key).asString());
	}
	
	public abstract void sendMessage(String... messages);
	
	public final void setData(String key, Object value) {
		setData(key, new Data(value));
	}
	
	public final void setData(String key, Data value) {
		this.data.put(key, value);
	}
}