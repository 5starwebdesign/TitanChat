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

package com.titankingdoms.nodinchan.titanchat;

import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import com.titankingdoms.nodinchan.titanchat.addon.AddonManager;
import com.titankingdoms.nodinchan.titanchat.channel.ChannelManager;
import com.titankingdoms.nodinchan.titanchat.command.CommandManager;

public class TitanChatManager {
	
	private final TitanChat plugin;
	
	private AddonManager addonManager;
	private ChannelManager chManager;
	private CommandManager cmdManager;
	
	public TitanChatManager() {
		this.plugin = TitanChat.getInstance();
	}
	
	public AddonManager getAddonManager() {
		return addonManager;
	}
	
	public ChannelManager getChannelManager() {
		return chManager;
	}
	
	public CommandManager getCommandManager() {
		return cmdManager;
	}
	
	public void load() {
		this.addonManager = new AddonManager();
		this.chManager = new ChannelManager();
		this.cmdManager = new CommandManager();
		
		Plugin ncbl = plugin.getServer().getPluginManager().getPlugin("NC-BukkitLib");
		
		if (ncbl != null) {
			addonManager.load();
			try { chManager.load(); } catch (Exception e) { e.printStackTrace(); plugin.log(Level.WARNING, "Channels failed to load"); }
			cmdManager.load();
		}
	}
	
	public void reload() {
		addonManager.preReload();
		chManager.preReload();
		cmdManager.preReload();
		addonManager.postReload();
		chManager.postReload();
		cmdManager.postReload();
	}
	
	public void unload() {
		this.addonManager.unload();
		this.chManager.unload();
		this.cmdManager.unload();
	}
}