package com.titankingdoms.nodinchan.titanchat.permission.bridges;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

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

public final class Bridge_Vault extends PermissionBridge {
	
	private Permission perm;
	private Chat chat;
	
	public Bridge_Vault() {
		super("Vault");
		
		Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");
		
		if (vault != null && vault.isEnabled()) {
			perm = plugin.getServer().getServicesManager().load(Permission.class);
			chat = plugin.getServer().getServicesManager().load(Chat.class);
			log(Level.INFO, getName() + " hooked");
		}
		
		plugin.getServer().getPluginManager().registerEvents(new ServerListener(new VaultChecker()), plugin);
	}
	
	@Override
	public String getPrefix(Player player, Permissible permissible) {
		if (permissible == null || perm == null || chat == null)
			return "";
		
		String prefix = "";
		
		try {
			switch (permissible) {
			
			case GROUP:
				String group = perm.getPrimaryGroup(player.getWorld().getName(), player.getName());
				prefix = chat.getGroupPrefix(player.getWorld().getName(), group);
				break;
				
			case PLAYER:
				prefix = chat.getPlayerPrefix(player.getWorld().getName(), player.getName());
				break;
			}
			
			return (prefix != null) ? prefix : "";
			
		} catch (Exception e) {
			return (prefix != null) ? prefix : "";
		}
	}
	
	@Override
	public String getSuffix(Player player, Permissible permissible) {
		if (permissible == null || perm == null || chat == null)
			return "";
		
		String suffix = "";
		
		try {
			switch (permissible) {
			
			case GROUP:
				String group = perm.getPrimaryGroup(player.getWorld().getName(), player.getName());
				suffix = chat.getGroupSuffix(player.getWorld().getName(), group);
				break;
				
			case PLAYER:
				suffix = chat.getPlayerSuffix(player.getWorld().getName(), player.getName());
				break;
			}
			
			return (suffix != null) ? suffix : "";
			
		} catch (Exception e) {
			return (suffix != null) ? suffix : "";
		}
	}
	
	@Override
	public boolean hasPermission(Player player, String permission) {
		try { return perm.playerHas(player.getWorld().getName(), player.getName(), permission); } catch (Exception e) { return player.hasPermission(permission); }
	}
	
	@Override
	public boolean isEnabled() {
		return perm != null && chat != null;
	}
	
	public final class VaultChecker extends PluginChecker {
		
		@Override
		public void onPluginDisable(Plugin plugin) {
			if (perm != null || chat != null) {
				if (plugin.getName().equals("Vault")) {
					perm = null;
					chat = null;
					log(Level.INFO, getName() + " unhooked");
				}
			}
		}
		
		@Override
		public void onPluginEnable(Plugin plugin) {
			if (perm == null || chat == null) {
				Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");
				
				if (vault != null && vault.isEnabled()) {
					perm = plugin.getServer().getServicesManager().load(Permission.class);
					chat = plugin.getServer().getServicesManager().load(Chat.class);
					log(Level.INFO, getName() + " hooked");
				}
			}
		}
	}
}