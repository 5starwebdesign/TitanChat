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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

public final class DataConversionHandler {
	
	private static final DataConverter COMMON = new DefaultConverter();
	
	private final Map<String, DataConverter> converters;
	
	public DataConversionHandler() {
		this.converters = new HashMap<>();
	}
	
	public DataConverter get(String key) {
		return (has(key)) ? converters.get(key) : COMMON;
	}
	
	public boolean has(String key) {
		return key != null && !key.isEmpty() && converters.containsKey(key);
	}
	
	public void register(DataConverter converter) {
		Validate.notNull(converter, "Converter cannot be null");
		Validate.isTrue(!has(converter.getKey()), "Converter already registered");
		
		converters.put(converter.getKey(), converter);
	}
	
	public void unregister(DataConverter converter) {
		Validate.notNull(converter, "Converter cannot be null");
		Validate.isTrue(has(converter.getKey()), "Converter not registered");
		
		converters.remove(converter.getKey());
	}
}