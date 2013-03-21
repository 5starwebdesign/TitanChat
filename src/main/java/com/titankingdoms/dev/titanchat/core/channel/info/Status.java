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

public enum Status {
	DEFAULT("default"),
	NONE("none"),
	STAFF("staff");
	
	private String name;
	private static final Map<String, Status> NAME_MAP = new HashMap<String, Status>();
	
	private Status(String name) {
		this.name = name;
	}
	
	static {
		for (Status type : EnumSet.allOf(Status.class))
			NAME_MAP.put(type.name, type);
	}
	
	public static Status fromName(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}
	
	public String getName() {
		return name;
	}
}