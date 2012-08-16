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

package com.titankingdoms.nodinchan.titanchat.util;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

import de.hydrox.bukkit.DroxPerms.DroxPerms;
import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;

public final class AffixFinder {
	
	private final TitanChat plugin;
	
	private final PluginManager pm;
	
	public AffixFinder() {
		this.plugin = TitanChat.getInstance();
		this.pm = plugin.getServer().getPluginManager();
	}
	
	public String getPrefix(Player player) {
		String prefix = "";
		
		if (pm.getPlugin("Vault") != null) {
			Permission perm = plugin.getServer().getServicesManager().load(Permission.class);
			Chat chat = plugin.getServer().getServicesManager().load(Chat.class);
			
			if (chat != null) {
				try {
					prefix = chat.getPlayerPrefix(player);
					
					if (prefix == null || prefix.isEmpty())
						prefix = chat.getGroupPrefix(player.getWorld(), perm.getPrimaryGroup(player));
					
				} catch (Exception e) {}
			}
		}
		
		if ((prefix == null || prefix.isEmpty()) && pm.getPlugin("PermissionsEx") != null)
			prefix = PermissionsEx.getPermissionManager().getUser(player).getPrefix();
		
		if ((prefix == null || prefix.isEmpty()) && pm.getPlugin("DroxPerms") != null) {
			DroxPermsAPI api = ((DroxPerms) pm.getPlugin("DroxPerms")).getAPI();
			
			prefix = api.getPlayerInfo(player.getName(), "prefix");
			
			if (prefix == null || prefix.isEmpty())
				prefix = api.getGroupInfo(api.getPlayerGroup(player.getName()), "prefix");
		}
		
		if ((prefix == null || prefix.isEmpty()) && pm.getPlugin("GroupManager") != null) {
			WorldsHolder wh = ((GroupManager) pm.getPlugin("GroupManager")).getWorldsHolder();
			AnjoPermissionsHandler handler = wh.getWorldPermissions(player);
			
			if (handler != null)
				prefix = handler.getUserPrefix(player.getName());
		}
		
		if (prefix == null || prefix.isEmpty())
			prefix = plugin.getInfoHandler().getInfo(player, "prefix", "");
		
		return prefix;
	}
	
	public String getSuffix(Player player) {
		String suffix = "";
		
		if (pm.getPlugin("Vault") != null) {
			Permission perm = plugin.getServer().getServicesManager().load(Permission.class);
			Chat chat = plugin.getServer().getServicesManager().load(Chat.class);
			
			if (chat != null) {
				try {
					suffix = chat.getPlayerSuffix(player);
					
					if (suffix == null || suffix.isEmpty())
						suffix = chat.getGroupSuffix(player.getWorld(), perm.getPrimaryGroup(player));
					
				} catch (Exception e) {}
			}
		}
		
		if ((suffix == null || suffix.isEmpty()) && pm.getPlugin("PermissionsEx") != null)
			suffix = PermissionsEx.getPermissionManager().getUser(player).getSuffix();
		
		if ((suffix == null || suffix.isEmpty()) && pm.getPlugin("DroxPerms") != null) {
			DroxPermsAPI api = ((DroxPerms) pm.getPlugin("DroxPerms")).getAPI();
			
			suffix = api.getPlayerInfo(player.getName(), "suffix");
			
			if (suffix == null || suffix.isEmpty())
				suffix = api.getGroupInfo(api.getPlayerGroup(player.getName()), "suffix");
		}
		
		if ((suffix == null || suffix.isEmpty()) && pm.getPlugin("GroupManager") != null) {
			WorldsHolder wh = ((GroupManager) pm.getPlugin("GroupManager")).getWorldsHolder();
			AnjoPermissionsHandler handler = wh.getWorldPermissions(player);
			
			if (handler != null)
				suffix = handler.getUserSuffix(player.getName());
		}
		
		if (suffix == null || suffix.isEmpty())
			suffix = plugin.getInfoHandler().getInfo(player, "suffix", "");
		
		return suffix;
	}
}