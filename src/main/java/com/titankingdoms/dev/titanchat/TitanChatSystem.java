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

package com.titankingdoms.dev.titanchat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.api.Manager;

public final class TitanChatSystem {
	
	private final Map<Class<?>, Manager<?>> managers = new LinkedHashMap<Class<?>, Manager<?>>();
	
	public <T extends Manager<?>> T getManager(Class<T> manager) {
		Validate.notNull(manager, "Manager Class cannot be null");
		return (hasManager(manager)) ? manager.cast(managers.get(manager)) : null;
	}
	
	public List<Manager<?>> getManagers() {
		synchronized (managers) {
			return new ArrayList<Manager<?>>(managers.values());
		}
	}
	
	public <T extends Manager<?>> boolean hasManager(Class<T> manager) {
		Validate.notNull(manager, "Manager Class cannot be null");
		
		synchronized (managers) {
			return managers.containsKey(manager);
		}
	}
	
	public void registerManager(Manager<?> manager) {
		Validate.notNull(manager, "Manager cannot be null");
		
		if (hasManager(manager.getClass()))
			return;
		
		managers.put(manager.getClass(), manager);
	}
	
	public void start() {}
	
	public void shutdown() {}
	
	public void unregisterManager(Manager<?> manager) {
		Validate.notNull(manager, "Manager cannot be null");
		
		if (!hasManager(manager.getClass()))
			return;
		
		managers.remove(manager.getClass());
	}
}