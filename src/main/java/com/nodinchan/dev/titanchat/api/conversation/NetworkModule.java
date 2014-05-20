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

package com.nodinchan.dev.titanchat.api.conversation;

import com.nodinchan.dev.conversation.Network;
import com.nodinchan.dev.module.Module;

public final class NetworkModule extends Network implements Module {
	
	private static final String NAME = "Network";
	
	private static final String[] DEPENDENCIES = new String[0];
	
	private boolean loaded;
	
	public NetworkModule() {
		this.loaded = false;
	}
	
	@Override
	public String[] getDependencies() {
		return DEPENDENCIES;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	@Override
	public void load() {}
	
	@Override
	public void reload() {}
	
	@Override
	public void setLoaded(boolean loaded) {
		if (this.loaded == loaded)
			return;
		
		this.loaded = loaded;
		
		if (loaded)
			load();
		else
			unload();
	}
	
	@Override
	public void unload() {}
}