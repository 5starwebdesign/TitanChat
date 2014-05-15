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

package com.nodinchan.dev.module.loadable;

import org.bukkit.plugin.Plugin;

import com.nodinchan.dev.tools.loading.Loadable;

public abstract class Addon extends Loadable {
	
	private boolean enabled;
	
	public Addon(Plugin plugin, String name) {
		super(plugin, name);
		this.enabled = false;
	}
	
	public final boolean isEnabled() {
		return enabled;
	}
	
	public void onDisable() {}
	
	public void onEnable() {}
	
	public void onReload() {}
	
	public final void setEnabled(boolean enabled) {
		if (this.enabled == enabled)
			return;
		
		this.enabled = enabled;
		
		if (enabled)
			onEnable();
		else
			onDisable();
	}
}