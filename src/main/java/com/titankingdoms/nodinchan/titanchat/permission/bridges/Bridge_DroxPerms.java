package com.titankingdoms.nodinchan.titanchat.permission.bridges;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.titankingdoms.nodinchan.titanchat.permission.PermissionBridge;

import de.hydrox.bukkit.DroxPerms.DroxPerms;
import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;

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

public final class Bridge_DroxPerms extends PermissionBridge {
	
	private DroxPermsAPI api;

	public Bridge_DroxPerms() {
		super("DroxPerms");
		
		Plugin perm = plugin.getServer().getPluginManager().getPlugin("DroxPerms");
		
		if (perm != null) {
			api = ((DroxPerms) perm).getAPI();
			log(Level.INFO, getName() + " hooked");
		}
		
		plugin.getServer().getPluginManager().registerEvents(new ServerListener(new DroxPermsChecker()), plugin);
	}
	
	@Override
	public String getPrefix(Player player, Permissible permissible) {
		if (permissible == null)
			return "";
		
		String prefix = "";
		
		switch (permissible) {
		
		case GROUP:
			prefix = api.getGroupInfo(api.getPlayerGroup(player.getName()), "prefix");
			break;
			
		case PLAYER:
			prefix = api.getPlayerInfo(player.getName(), "prefix");
			break;
		}
		
		return (prefix != null) ? prefix : "";
	}
	
	@Override
	public String getSuffix(Player player, Permissible permissible) {
		if (permissible == null)
			return "";
		
		String prefix = "";
		
		switch (permissible) {
		
		case GROUP:
			prefix = api.getGroupInfo(api.getPlayerGroup(player.getName()), "suffix");
			break;
			
		case PLAYER:
			prefix = api.getPlayerInfo(player.getName(), "suffix");
			break;
		}
		
		return (prefix != null) ? prefix : "";
	}
	
	@Override
	public boolean hasPermission(Player player, String permission) {
		return player.hasPermission(permission);
	}
	
	public final class DroxPermsChecker extends PluginChecker {
		
		@Override
		public void onPluginDisable(Plugin plugin) {
			if (api != null) {
				if (plugin.getName().equals("DroxPerms")) {
					api = null;
					log(Level.INFO, getName() + " unhooked");
				}
			}
		}
		
		@Override
		public void onPluginEnable(Plugin plugin) {
			if (api == null) {
				Plugin perm = plugin.getServer().getPluginManager().getPlugin("DroxPerms");
				
				if (perm != null) {
					api = ((DroxPerms) perm).getAPI();
					log(Level.INFO, getName() + " hooked");
				}
			}
		}
	}
}