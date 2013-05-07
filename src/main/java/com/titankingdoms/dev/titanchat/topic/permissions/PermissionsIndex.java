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

package com.titankingdoms.dev.titanchat.topic.permissions;

import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.topic.Index;

/**
 * {@link PermissionsIndex} - Index of permissions
 * 
 * @author NodinChan
 *
 */
public final class PermissionsIndex extends Index {
	
	public PermissionsIndex() {
		super("Permissions");
	}
	
	/**
	 * Indexes all registered TitanChat permissions
	 * 
	 * @return This {@link PermissionsIndex}
	 */
	public PermissionsIndex index() {
		YamlConfiguration ps = YamlConfiguration.loadConfiguration(plugin.getResource("permissions.yml"));
		
		for (String name : ps.getKeys(false)) {
			String node = "TitanChat." + name.replace("_", ".");
			String description = ps.getString(name + ".description");
			addTopic(new PermissionTopic(node, description));
		}
		
		return this;
	}
}