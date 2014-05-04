/*
 *     Copyright (C) 2014  Nodin Chan
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

package com.titankingdoms.dev.titanchat.tools.metadata;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableMap;

public final class Metadata {
	
	private final Map<String, Data> metadata;
	
	public Metadata() {
		this.metadata = new TreeMap<>();
	}
	
	public Data get(String key) {
		return (has(key)) ? metadata.get(key) : null;
	}
	
	public boolean has(String key) {
		return key != null && !key.isEmpty() && metadata.containsKey(key);
	}
	
	public Map<String, Data> map() {
		return ImmutableMap.copyOf(metadata);
	}
	
	public Metadata remove(String key) {
		Validate.notEmpty(key, "Key cannot be empty");
		metadata.remove(key);
		return this;
	}
	
	public Metadata set(String key, String value) {
		Validate.notEmpty(key, "Key cannot be empty");
		
		if (value == null)
			return remove(key);
		
		if (!metadata.containsKey(key))
			metadata.put(key, new Data());
		
		metadata.get(key).set(value);
		return this;
	}
	
	public Metadata set(String key, Data data) {
		Validate.notEmpty(key, "Key cannot be empty");
		
		if (data == null)
			return remove(key);
		
		metadata.put(key, data);
		return this;
	}
	
	public String value(String key) {
		return (has(key)) ? metadata.get(key).value() : null;
	}
}