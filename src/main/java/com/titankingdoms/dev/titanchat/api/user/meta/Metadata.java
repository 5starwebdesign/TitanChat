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

package com.titankingdoms.dev.titanchat.api.user.meta;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.api.user.User;

public final class Metadata {
	
	private final User user;
	
	private final Map<String, Meta> data;
	
	public Metadata(User user) {
		Validate.notNull(user, "User cannot be null");
		
		this.user = user;
		this.data = new LinkedHashMap<String, Meta>();
	}
	
	public Meta getData(String key) {
		return (hasData(key)) ? data.get(key) : null;
	}
	
	public Map<String, Meta> getData() {
		return new LinkedHashMap<String, Meta>(data);
	}
	
	public User getUser() {
		return user;
	}
	
	public boolean hasData(String key) {
		return key != null && !key.isEmpty() && data.containsKey(key);
	}
	
	public void setData(String key, Meta meta) {
		Validate.notEmpty(key, "Key cannot be empty");
		
		if (meta != null)
			data.put(key, meta);
		else
			data.remove(key);
	}
	
	public void setData(String key, String value) {
		setData(key, (value != null) ? new Meta(value) : null);
	}
	
	public static class Meta {
		
		private final String value;
		
		public Meta(String value) {
			this.value = (value != null) ? value : "";
		}
		
		public final String getValue() {
			return value;
		}
	}
}