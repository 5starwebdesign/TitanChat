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

package com.nodinchan.dev.module.event;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.nodinchan.dev.module.Module;

public class ModuleEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Module module;
	
	private final String type;
	
	public ModuleEvent(Module module, String type) {
		super(!Bukkit.isPrimaryThread());
		Validate.notNull(module, "Module cannot be null");
		Validate.notEmpty(type, "Type cannot be empty");
		
		this.module = module;
		this.type = type;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public final Module getModule() {
		return module;
	}
	
	public final String getType() {
		return type;
	}
}