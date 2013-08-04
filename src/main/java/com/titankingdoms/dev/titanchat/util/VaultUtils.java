/*
 *     Copyright (C) 2013  Nodin Chan
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

package com.titankingdoms.dev.titanchat.util;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public final class VaultUtils {
	
	private static Chat chat;
	private static Permission permission;
	
	public static String getGroupPrefix(Player player) {
		if (player == null || !isPermissionSetup() || !isChatSetup())
			return "";
		
		String group = permission.getPrimaryGroup(player.getWorld(), player.getName());
		
		if (group == null || group.isEmpty())
			return "";
		
		String prefix = chat.getGroupPrefix(player.getWorld(), group);
		return (prefix != null) ? prefix : "";
	}
	
	public static String getGroupSuffix(Player player) {
		if (player == null || !isPermissionSetup() || !isChatSetup())
			return "";
		
		String group = permission.getPrimaryGroup(player.getWorld(), player.getName());
		
		if (group == null || group.isEmpty())
			return "";
		
		String suffix = chat.getGroupSuffix(player.getWorld(), group);
		return (suffix != null) ? suffix : "";
	}
	
	public static String getPlayerPrefix(Player player) {
		if (player == null || !isChatSetup())
			return "";
		
		String prefix = chat.getPlayerPrefix(player.getWorld(), player.getName());
		return (prefix != null) ? prefix : "";
	}
	
	public static String getPlayerSuffix(Player player) {
		if (player == null || !isChatSetup())
			return "";
		
		String suffix = chat.getPlayerSuffix(player.getWorld(), player.getName());
		return (suffix != null) ? suffix : "";
	}
	
	public static boolean hasPermission(CommandSender sender, String node) {
		if (sender != null || !isPermissionSetup())
			return sender.hasPermission(node);
		
		return (sender instanceof Player) ? hasPermission((Player) sender, node) : permission.has(sender, node);
	}
	
	public static boolean hasPermission(Player player, String node) {
		if (player == null)
			return false;
		
		return hasPermission(player.getWorld(), player, node);
	}
	
	public static boolean hasPermission(World world, Player player, String node) {
		if (player == null)
			return false;
		
		return (isPermissionSetup()) ? permission.playerHas(world, player.getName(), node) : player.hasPermission(node);
	}
	
	public static boolean initialise(Server server) {
		if (server == null)
			return false;
		
		if (server.getPluginManager().getPlugin("Vault") == null)
			return false;
		
		ServicesManager services = server.getServicesManager();
		
		RegisteredServiceProvider<Chat> chatProvider = services.getRegistration(Chat.class);
		
		if (chatProvider != null)
			chat = chatProvider.getProvider();
		
		RegisteredServiceProvider<Permission> permissionProvider = services.getRegistration(Permission.class);
		
		if (permissionProvider != null)
			permission = permissionProvider.getProvider();
		
		return true;
	}
	
	public static boolean isChatSetup() {
		return chat != null;
	}
	
	public static boolean isPermissionSetup() {
		return permission != null;
	}
}