package com.titankingdoms.nodinchan.titanchat.permission.bridges;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

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

public final class Bridge_PermissionsEx extends PermissionBridge {
	
	private PermissionsEx pex;
	
	public Bridge_PermissionsEx() {
		super("PermissionsEx");
		
		Plugin perm = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
		
		if (perm != null && perm.isEnabled()) {
			pex = (PermissionsEx) perm;
			log(Level.INFO, getName() + " hooked");
		}
		
		plugin.getServer().getPluginManager().registerEvents(new ServerListener(new PermissionsExChecker()), plugin);
	}
	
	@Override
	public String getPrefix(Player player, Permissible permissible) {
		if (permissible == null)
			return "";
		
		PermissionUser user = PermissionsEx.getUser(player);
		
		if (user == null)
			return "";
		
		switch (permissible) {
		
		case GROUP:
			PermissionGroup[] groups = user.getGroups();
			return (groups.length > 0) ? groups[0].getPrefix(player.getWorld().getName()) : "";
			
		case PLAYER:
			return user.getPrefix(player.getWorld().getName());
		}
		
		return "";
	}
	
	@Override
	public String getSuffix(Player player, Permissible permissible) {
		PermissionUser user = PermissionsEx.getUser(player.getName());
		return (user != null) ? user.getSuffix(player.getWorld().getName()) : "";
	}
	
	@Override
	public boolean hasPermission(Player player, String permission) {
		PermissionUser user = PermissionsEx.getUser(player.getName());
		return (user != null) ? user.has(permission, player.getWorld().getName()) : false;
	}
	
	@Override
	public boolean isEnabled() {
		return pex != null && pex.isEnabled();
	}
	
	public final class PermissionsExChecker extends PluginChecker {
		
		@Override
		public void onPluginDisable(Plugin plugin) {
			if (pex != null) {
				if (plugin.getName().equals("PermissionsEx")) {
					pex = null;
					log(Level.INFO, getName() + " unhooked");
				}
			}
		}
		
		@Override
		public void onPluginEnable(Plugin plugin) {
			if (pex == null) {
				Plugin perm = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
				
				if (perm != null && perm.isEnabled()) {
					pex = (PermissionsEx) perm;
					log(Level.INFO, getName() + " hooked");
				}
			}
		}
	}
}