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

package com.nodinchan.dev.titanchat;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.nodinchan.dev.module.ModuleManager;

public final class TitanChat extends JavaPlugin {
	
	private static TitanChat instance;
	private static ModuleManager manager;
	
	public TitanChat getInstance() {
		return instance();
	}
	
	public ModuleManager getModuleManager() {
		return module();
	}
	
	public static TitanChat instance() {
		if (instance == null)
			throw new IllegalStateException("TitanChat is not in operation");
		
		return instance;
	}
	
	public void log(Level level, String message) {
		if (message == null || message.isEmpty())
			return;
		
		getLogger().log((level != null) ? level : Level.INFO, message);
	}
	
	public static ModuleManager module() {
		if (instance == null)
			throw new IllegalStateException("TitanChat is not in operation");
		
		return manager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}
	
	@Override
	public void onDisable() {
		if (instance != null)
			instance = null;
	}
	
	@Override
	public void onEnable() {
		if (instance == null)
			instance = this;
	}
	
	@Override
	public void onLoad() {
		if (instance == null)
			instance = this;
	}
}