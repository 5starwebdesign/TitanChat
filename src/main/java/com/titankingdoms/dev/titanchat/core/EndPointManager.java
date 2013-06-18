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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EndPointManager {
	
	private static final Map<String, EndPointSearch> search = new HashMap<String, EndPointSearch>();
	
	private static EndPointSearch getSearch(String pointType) {
		return search.get((pointType != null) ? pointType.toLowerCase() : "");
	}
	
	public static List<String> getSearchTypes() {
		List<String> list = new ArrayList<String>(search.keySet());
		Collections.sort(list);
		return list;
	}
	
	public static boolean hasSearch(String pointType) {
		return search.containsKey((pointType != null) ? pointType.toLowerCase() : "");
	}
	
	public static void registerSearch(EndPointSearch search) {
		if (search == null || hasSearch(search.getPointType()))
			return;
		
		EndPointManager.search.put(search.getPointType().toLowerCase(), search);
	}
	
	public static EndPoint search(String pointType, String name) {
		return (hasSearch(pointType)) ? getSearch(pointType).getEndPoint((name != null) ? name : "") : null;
	}
	
	public static void unregisterSearch(EndPointSearch search) {
		if (search == null || !hasSearch(search.getPointType()))
			return;
		
		EndPointManager.search.remove(search.getPointType().toLowerCase());
	}
	
	public static interface EndPointSearch {
		
		public EndPoint getEndPoint(String name);
		
		public List<EndPoint> getEndPoints();
		
		public boolean hasEndPoint(String name);
		
		public String getPointType();
	}
}