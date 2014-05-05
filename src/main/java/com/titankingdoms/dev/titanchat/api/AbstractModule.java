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

package com.titankingdoms.dev.titanchat.api;

public abstract class AbstractModule implements Module {
	
	private static final String[] DEPENDENCIES = new String[0];
	
	private final String name;
	
	private boolean loaded;
	
	public AbstractModule(String name) {
		this.name = name;
		this.loaded = false;
	}
	
	@Override
	public String[] getDependencies() {
		return DEPENDENCIES;
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public final boolean isLoaded() {
		return loaded;
	}
	
	@Override
	public final void setLoaded(boolean loaded) {
		if (this.loaded == loaded)
			return;
		
		this.loaded = loaded;
		
		if (loaded)
			load();
		else
			unload();
	}
}