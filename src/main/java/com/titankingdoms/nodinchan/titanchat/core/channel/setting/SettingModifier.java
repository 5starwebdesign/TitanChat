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

package com.titankingdoms.nodinchan.titanchat.core.channel.setting;

import java.util.Map;
import java.util.TreeMap;

public final class SettingModifier {
	
	private final Map<String, Setting> settings;
	
	public SettingModifier() {
		this.settings = new TreeMap<String, Setting>();
	}
	
	public boolean existingSetting(String name) {
		return settings.containsKey(name.toLowerCase());
	}
	
	private boolean existingSetting(Setting setting) {
		return existingSetting(setting.getName());
	}
	
	public Setting getSetting(String name) {
		return settings.get(name.toLowerCase());
	}
	
	public void registerSettings(Setting... settings) {
		for (Setting setting : settings) {
			if (existingSetting(setting))
				continue;
			
			this.settings.put(setting.getName().toLowerCase(), setting);
		}
	}
}