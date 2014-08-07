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

package com.nodinchan.dev.module.loading;

import java.io.InputStream;

import com.nodinchan.dev.module.Module;

public abstract class LoadableModule implements Module {
	
	private final String name;
	
	protected boolean loaded;
	
	public LoadableModule(String name) {
		this.name = name;
		this.loaded = false;
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	public final InputStream getResource(String name) {
		return getClass().getClassLoader().getResourceAsStream(name);
	}
	
	@Override
	public final boolean isLoaded() {
		return loaded;
	}
	
	@Override
	public void load() {}
	
	@Override
	public boolean setLoaded(boolean loaded) {
		if (this.loaded == loaded)
			return false;
		
		this.loaded = loaded;
		
		if (loaded)
			load();
		else
			unload();
		
		return true;
	}
	
	@Override
	public void unload() {}
}