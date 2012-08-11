package com.titankingdoms.nodinchan.titanchat.permission.bridges;

import java.util.logging.Level;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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

public final class Bridge_GroupManager extends PermissionBridge {
	
	private GroupManager gm;
	
	public Bridge_GroupManager() {
		super("GroupManager");
		
		Plugin perm = plugin.getServer().getPluginManager().getPlugin("GroupManager");
		
		if (perm != null && perm.isEnabled()) {
			gm = (GroupManager) perm;
			log(Level.INFO, getName() + " hooked");
		}
		
		plugin.getServer().getPluginManager().registerEvents(new ServerListener(new GroupManagerChecker()), plugin);
	}
	
	@Override
	public String getPrefix(Player player, Permissible permissible) {
		if (permissible == null)
			return "";
		
		AnjoPermissionsHandler handler;
		
		switch (permissible) {
		
		case GROUP:
			handler = gm.getWorldsHolder().getWorldPermissions(player.getWorld().getName());
			
			if (handler == null)
				return "";
			
			String group = handler.getGroup(player.getName());
			return handler.getGroupPrefix(group);
			
		case PLAYER:
			handler = gm.getWorldsHolder().getWorldPermissionsByPlayerName(player.getName());
			
			if (handler == null)
				return "";
			
			return handler.getUserPrefix(player.getName());
		}
		
		return "";
	}
	
	@Override
	public String getSuffix(Player player, Permissible permissible) {
		if (permissible == null)
			return "";
		
		AnjoPermissionsHandler handler;
		
		switch (permissible) {
		
		case GROUP:
			handler = gm.getWorldsHolder().getWorldPermissions(player.getWorld().getName());
			
			if (handler == null)
				return "";
			
			String group = handler.getGroup(player.getName());
			return handler.getGroupSuffix(group);
			
		case PLAYER:
			handler = gm.getWorldsHolder().getWorldPermissionsByPlayerName(player.getName());
			
			if (handler == null)
				return "";
			
			return handler.getUserSuffix(player.getName());
		}
		
		return "";
	}
	
	@Override
	public boolean hasPermission(Player player, String permission) {
		AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissionsByPlayerName(player.getName());
		return (handler != null) ? handler.permission(player, permission) : false;
	}
	
	public final class GroupManagerChecker extends PluginChecker {
		
		@Override
		public void onPluginDisable(Plugin plugin) {
			if (gm != null) {
				if (plugin.getName().equals("GroupManager")) {
					gm = null;
					log(Level.INFO, getName() + " unhooked");
				}
			}
		}
		
		@Override
		public void onPluginEnable(Plugin plugin) {
			if (gm == null) {
				Plugin perm = plugin.getServer().getPluginManager().getPlugin("GroupManager");
				
				if (perm != null && perm.isEnabled()) {
					gm = (GroupManager) perm;
					log(Level.INFO, getName() + " hooked");
				}
			}
		}
	}
}