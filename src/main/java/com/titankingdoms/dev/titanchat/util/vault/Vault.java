/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat.util.vault;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

/**
 * {@link Vault} - Vault bridge
 * 
 * @author NodinChan
 *
 */
public final class Vault {
	
	private static Chat chat;
	private static Permission permission;
	
	/**
	 * Gets the group prefix of the {@link Player}
	 * 
	 * @param player The {@link Player} to get for
	 * 
	 * @return The prefix if found
	 */
	public static String getGroupPrefix(Player player) {
		if (player == null || !isPermissionSetup() || !isChatSetup())
			return "";
		
		String group = permission.getPrimaryGroup(player.getWorld(), player.getName());
		
		if (group == null || group.isEmpty())
			return "";
		
		String prefix = chat.getGroupPrefix(player.getWorld(), group);
		return (prefix != null) ? prefix : "";
	}
	
	/**
	 * Gets the group suffix of the {@link Player}
	 * 
	 * @param player The {@link Player} to get for
	 * 
	 * @return The suffix if found
	 */
	public static String getGroupSuffix(Player player) {
		if (player == null || !isPermissionSetup() || !isChatSetup())
			return "";
		
		String group = permission.getPrimaryGroup(player.getWorld(), player.getName());
		
		if (group == null || group.isEmpty())
			return "";
		
		String suffix = chat.getGroupSuffix(player.getWorld(), group);
		return (suffix != null) ? suffix : "";
	}
	
	/**
	 * Gets the player prefix of the {@link Player}
	 * 
	 * @param player The {@link Player} to get for
	 * 
	 * @return The prefix if found
	 */
	public static String getPlayerPrefix(Player player) {
		if (player == null || !isChatSetup())
			return "";
		
		String prefix = chat.getPlayerPrefix(player.getWorld(), player.getName());
		return (prefix != null) ? prefix : "";
	}
	
	/**
	 * Gets the player suffix of the {@link Player}
	 * 
	 * @param player The {@link Player} to get for
	 * 
	 * @return The suffix if found
	 */
	public static String getPlayerSuffix(Player player) {
		if (player == null || !isChatSetup())
			return "";
		
		String suffix = chat.getPlayerSuffix(player.getWorld(), player.getName());
		return (suffix != null) ? suffix : "";
	}
	
	/**
	 * Checks if the {@link CommandSender} has the permission node
	 * 
	 * @param sender The {@link CommandSender} to check
	 * 
	 * @param node The permission node
	 * 
	 * @return True if the {@link CommandSender} has permission
	 */
	public static boolean hasPermission(CommandSender sender, String node) {
		if (sender != null || !isPermissionSetup())
			return sender.hasPermission(node);
		
		return (sender instanceof Player) ? hasPermission((Player) sender, node) : permission.has(sender, node);
	}
	
	/**
	 * Checks if the {@link Player} has the permission node
	 * 
	 * @param player The {@link Player} to check
	 * 
	 * @param node The permission node
	 * 
	 * @return True if the {@link Player} has permission
	 */
	public static boolean hasPermission(Player player, String node) {
		return hasPermission(player.getWorld(), player, node);
	}
	
	/**
	 * Checks if the {@link Player} has the permission node in the specified {@link World}
	 * 
	 * @param world The {@link World}
	 * 
	 * @param player The {@link Player} to check
	 * 
	 * @param node The permission node
	 * 
	 * @return True if the {@link Player} has permission
	 */
	public static boolean hasPermission(World world, Player player, String node) {
		if (player == null)
			return false;
		
		return (isPermissionSetup()) ? permission.playerHas(world, player.getName(), node) : player.hasPermission(node);
	}
	
	/**
	 * Initialises the Vault bridge
	 * 
	 * @param server The {@link Server}
	 * 
	 * @return True if initialised
	 */
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
	
	/**
	 * Checks if {@link Chat} is set up
	 * 
	 * @return True if is set up
	 */
	public static boolean isChatSetup() {
		return chat != null;
	}
	
	/**
	 * Checks if {@link Permission} is set up
	 * 
	 * @return True if is set up
	 */
	public static boolean isPermissionSetup() {
		return permission != null;
	}
}