/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat.util.info;

import java.util.HashMap;
import java.util.Map;

public final class CachedInfo {
	
	private final String name;
	
	private final Map<String, String> infoMap;
	
	public CachedInfo(String name) {
		this.name = name;
		this.infoMap = new HashMap<String, String>();
	}
	
	public String getInfo(String infoType) {
		return infoMap.get(infoType.toLowerCase());
	}
	
	public String getName() {
		return name;
	}
	
	public void setInfo(String infoType, String newInfo) {
		infoMap.put(infoType.toLowerCase(), newInfo);
	}
}