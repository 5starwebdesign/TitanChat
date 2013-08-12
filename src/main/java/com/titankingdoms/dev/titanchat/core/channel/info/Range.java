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

package com.titankingdoms.dev.titanchat.core.channel.info;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Range} - The {@link Channel} communication range
 * 
 * @author NodinChan
 *
 */
public enum Range {
	CHANNEL("channel"),
	GLOBAL("global"),
	LOCAL("local"),
	WORLD("world");
	
	private String name;
	private static final Map<String, Range> NAME_MAP = new HashMap<String, Range>();
	
	private Range(String name) {
		this.name = name;
	}
	
	static {
		for (Range range : EnumSet.allOf(Range.class))
			NAME_MAP.put(range.name, range);
	}
	
	public static Range fromName(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}
	
	public String getName() {
		return name;
	}
}