package com.titankingdoms.nodinchan.titanchat.command.commands;

import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.addon.AddonManager;
import com.titankingdoms.nodinchan.titanchat.channel.ChannelManager;
import com.titankingdoms.nodinchan.titanchat.command.Command;
import com.titankingdoms.nodinchan.titanchat.command.CommandManager;
import com.titankingdoms.nodinchan.titanchat.command.info.CommandID;
import com.titankingdoms.nodinchan.titanchat.command.info.CommandInfo;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;
import com.titankingdoms.nodinchan.titanchat.util.FormatHandler;
import com.titankingdoms.nodinchan.titanchat.util.variable.Variable;

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

/**
 * PluginCommand - Plugin maintenance commands
 * 
 * @author NodinChan
 *
 */
public class PluginCommand extends Command {
	
	private AddonManager am;
	private ChannelManager chm;
	private CommandManager cmdm;
	private FormatHandler format;
	private Variable var;
	
	public PluginCommand() {
		this.am = plugin.getManager().getAddonManager();
		this.chm = plugin.getManager().getChannelManager();
		this.cmdm = plugin.getManager().getCommandManager();
		this.format = plugin.getFormatHandler();
		this.var = plugin.getVariableManager();
	}
	
	/**
	 * Debug Command - Toggles the debug
	 */
	@CommandID(name = "Debug", aliases = "debug")
	@CommandInfo(description = "Toggles the debug", usage = "debug [type]")
	public void debug(Player player, String[] args) {
		try {
			if (plugin.isStaff(player)) {
				if (args[0].equalsIgnoreCase("none")) {
					Debugger.disable();
					plugin.sendInfo(player, "Debug activated");
					
				} else if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("full")) {
					Debugger.enableAll();
					plugin.sendInfo(player, "Debug activated");
					
				} else {
					for (String id : args[0].split(","))
						Debugger.enable(id);
					
					plugin.sendInfo(player, "Debug activated");
				}
				
			} else { plugin.sendWarning(player, "You do not have permission"); }
			
		} catch (IndexOutOfBoundsException e) { invalidArgLength(player, "Debug"); }
	}
	
	/**
	 * Reload Command - Reloads the config
	 */
	@CommandID(name = "Reload", aliases = "reload")
	@CommandInfo(description = "Reloads the config", usage = "reload")
	public void reload(Player player, String[] args) {
		if (plugin.isStaff(player)) {
			plugin.log(Level.INFO, "Reloading configs...");
			plugin.sendInfo(player, "Reloading configs...");
			plugin.reloadConfig();
			am.preReload();
			chm.preReload();
			cmdm.preReload();
			var.unload();
			format.load();
			am.postReload();
			chm.postReload();
			cmdm.postReload();
			plugin.log(Level.INFO, "Configs reloaded");
			plugin.sendInfo(player, "Configs reloaded");
			
		} else { plugin.sendWarning(player, "You do not have permission"); }
	}
}