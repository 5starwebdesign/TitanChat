package com.titankingdoms.nodinchan.titanchat.permission.bridges;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.titankingdoms.nodinchan.titanchat.permission.PermissionBridge;

import net.krinsoft.privileges.Privileges;

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

public final class Bridge_Privileges extends PermissionBridge {
	
	private Privileges prvlgs;
	
	public Bridge_Privileges() {
		super("Privileges");
		
		Plugin perm = plugin.getServer().getPluginManager().getPlugin("Privileges");
		
		if (perm != null && perm.isEnabled()) {
			prvlgs = (Privileges) perm;
			log(Level.INFO, getName() + " hooked");
		}
		
		plugin.getServer().getPluginManager().registerEvents(new ServerListener(new PrivilegesChecker()), plugin);
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
		return prvlgs != null && prvlgs.isEnabled();
	}
	
	public final class PrivilegesChecker extends PluginChecker {
		
		@Override
		public void onPluginDisable(Plugin plugin) {
			if (prvlgs != null) {
				if (plugin.getName().equals("Privileges")) {
					prvlgs = null;
					log(Level.INFO, getName() + " unhooked");
				}
			}
		}
		
		@Override
		public void onPluginEnable(Plugin plugin) {
			if (prvlgs == null) {
				Plugin perm = plugin.getServer().getPluginManager().getPlugin("Privileges");
				
				if (perm != null && perm.isEnabled()) {
					prvlgs = (Privileges) perm;
					log(Level.INFO, getName() + " hooked");
				}
			}
		}
	}
}