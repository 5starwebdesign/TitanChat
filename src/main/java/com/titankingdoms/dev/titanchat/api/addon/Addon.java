/*
 *     Copyright (C) 2013  Nodin Chan
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

package com.titankingdoms.dev.titanchat.api.addon;

import com.titankingdoms.dev.titanchat.tools.loading.Loadable;

public class Addon extends Loadable {
	
	private boolean enabled;
	
	public Addon(String name) {
		super(name);
		this.enabled = false;
	}
	
	@Override
	public boolean equals(Object object) {
		return (object instanceof Addon) ? toString().equals(object.toString()) : false;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	public final boolean isEnabled() {
		return enabled;
	}
	
	public void onDisable() {
		setEnabled(false);
	}
	
	public void onEnable() {
		setEnabled(true);
	}
	
	public void onReload() {}
	
	private final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public String toString() {
		return "\"Addon\": {" +
				"\"name\": \"" + getName() + "\", " +
				"\"file\": \"" + getFile().getName() + "\"" +
				"}";
	}
}