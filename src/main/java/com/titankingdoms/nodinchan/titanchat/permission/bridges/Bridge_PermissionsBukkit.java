package com.titankingdoms.nodinchan.titanchat.permission.bridges;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.platymuus.bukkit.permissions.PermissionsPlugin;
import com.titankingdoms.nodinchan.titanchat.permission.PermissionBridge;

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

public final class Bridge_PermissionsBukkit extends PermissionBridge {
	
	private PermissionsPlugin permBukkit;
	
	public Bridge_PermissionsBukkit() {
		super("PermissionsBukkit");
		
		Plugin perm = plugin.getServer().getPluginManager().getPlugin("PermissionsBukkit");
		
		if (perm != null && perm.isEnabled()) {
			permBukkit = (PermissionsPlugin) perm;
			log(Level.INFO, getName() + " hooked");
		}
		
		plugin.getServer().getPluginManager().registerEvents(new ServerListener(new PermissionsBukkitChecker()), plugin);
	}
	
	@Override
	public String getPrefix(Player player, Permissible permissible) {
		return "";
	}
	
	@Override
	public String getSuffix(Player player, Permissible permissible) {
		return "";
	}
	
	@Override
	public boolean hasPermission(Player player, String permission) {
		return player.hasPermission(permission);
	}
	
	@Override
	public boolean isEnabled() {
		return permBukkit != null && permBukkit.isEnabled();
	}
	
	public final class PermissionsBukkitChecker extends PluginChecker {
		
		@Override
		public void onPluginDisable(Plugin plugin) {
			if (permBukkit != null) {
				if (plugin.getName().equals("PermissionsBukkit")) {
					permBukkit = null;
					log(Level.INFO, getName() + " unhooked");
				}
			}
		}
		
		@Override
		public void onPluginEnable(Plugin plugin) {
			if (permBukkit == null) {
				Plugin perm = plugin.getServer().getPluginManager().getPlugin("PermissionsBukkit");
				
				if (perm != null && perm.isEnabled()) {
					permBukkit = (PermissionsPlugin) perm;
					log(Level.INFO, getName() + " hooked");
				}
			}
		}
	}
}