/*
 *     Copyright (C) 2013  Nodin Chan
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
 *     along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.titankingdoms.dev.titanchat.core.user;

import org.bukkit.configuration.ConfigurationSection;

public final class Metadata {
	
	private final ConfigurationSection metadata;
	
	public Metadata(ConfigurationSection metadata) {
		this.metadata = metadata;
	}
	
	public Object get(String key, Object def) {
		return (contains(key)) ? metadata.get(key) : def;
	}
	
	public Object get(String key) {
		return get(key, null);
	}
	
	public boolean getBoolean(String key, boolean def) {
		return (contains(key)) ? metadata.getBoolean(key) : def;
	}
	
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	
	public double getDouble(String key, double def) {
		return (contains(key)) ? metadata.getDouble(key) : def;
	}
	
	public double getDouble(String key) {
		return getDouble(key, 0.0D);
	}
	
	public int getInt(String key, int def) {
		return (contains(key)) ? metadata.getInt(key) : def;
	}
	
	public int getInt(String key) {
		return getInt(key, 0);
	}
	
	public long getLong(String key, long def) {
		return (contains(key)) ? metadata.getLong(key) : def;
	}
	
	public long getLong(String key) {
		return getLong(key, 0L);
	}
	
	public String getString(String key, String def) {
		return (contains(key)) ? metadata.getString(key) : def;
	}
	
	public String getString(String key) {
		return getString(key, "");
	}
	
	public boolean contains(String key) {
		return (metadata != null && key != null) ? metadata.get(key, null) != null : false;
	}
	
	public void set(String key, Object value) {
		if (metadata == null)
			return;
		
		metadata.set(key, value);
	}
}