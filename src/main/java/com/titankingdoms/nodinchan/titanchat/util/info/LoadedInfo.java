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

import org.bukkit.configuration.ConfigurationSection;

public final class LoadedInfo extends InfoBase implements Comparable<LoadedInfo> {
	
	private final InfoHandler handler;
	
	private final String path;
	
	private final String permission;
	
	private final int priority;
	
	public LoadedInfo(InfoHandler handler, String section) {
		super();
		this.handler = handler;
		this.path = "permission-specific." + section;
		this.permission = "TitanChat.info." + section;
		this.priority = handler.getInfo(path + ".priority", 0);
	}
	
	public int compareTo(LoadedInfo loadedInfo) {
		if (priority == loadedInfo.priority)
			return 0;
		
		return (priority > loadedInfo.priority) ? 1 : -1;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public ConfigurationSection getSection() {
		return handler.getSection(path);
	}
}