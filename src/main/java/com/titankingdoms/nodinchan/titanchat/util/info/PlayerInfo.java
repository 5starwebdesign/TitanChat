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

public final class PlayerInfo extends InfoBase {
	
	private final InfoHandler handler;
	
	private final String name;
	
	public PlayerInfo(InfoHandler handler, String name) {
		this.handler = handler;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public ConfigurationSection getSection() {
		return handler.getSection("player-specific." + name);
	}
}