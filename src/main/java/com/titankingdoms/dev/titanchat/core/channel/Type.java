/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat.core.channel;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Type {
	CUSTOM("custom"),
	DEFAULT("default"),
	NONE("none"),
	STAFF("staff");
	
	private String name;
	private static Map<String, Type> NAME_MAP = new HashMap<String, Type>();
	
	private Type(String name) {
		this.name = name;
	}
	
	static {
		for (Type type : EnumSet.allOf(Type.class))
			NAME_MAP.put(type.name.toLowerCase(), type);
	}
	
	public static Type fromName(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}
	
	public String getName() {
		return name;
	}
}