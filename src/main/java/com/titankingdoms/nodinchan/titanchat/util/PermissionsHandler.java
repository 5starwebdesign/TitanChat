package com.titankingdoms.nodinchan.titanchat.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.permission.PermissionBridge;
import com.titankingdoms.nodinchan.titanchat.permission.PermissionBridge.Permissible;
import com.titankingdoms.nodinchan.titanchat.permission.bridges.*;

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

public final class PermissionsHandler {
	
	private final TitanChat plugin;
	
	private static final Debugger db = new Debugger(5);
	
	private final Map<String, Affix> groupAffixes;
	private final Map<String, Affix> playerAffixes;
	private final Map<String, Affix> playersAffixes;
	
	private PermissionBridge permission;
	
	public PermissionsHandler() {
		this.plugin = TitanChat.getInstance();
		this.groupAffixes = new HashMap<String, Affix>();
		this.playerAffixes = new HashMap<String, Affix>();
		this.playersAffixes = new HashMap<String, Affix>();
	}
	
	public String getGroupPrefix(Player player) {
		String prefix = "";
		db.i("PermissionsHandler: Getting group prefix of " + player.getName());
		
		int lastPriority = -1;
		
		for (String permission : groupAffixes.keySet()) {
			if (!has(player, permission))
				continue;
			
			Affix affix = groupAffixes.get(permission);
			
			int priority = affix.getPriority();
			
			if (priority > lastPriority) {
				prefix = affix.getPrefix();
				lastPriority = priority;
			}
		}
		
		if (prefix.isEmpty())
			prefix = permission.getPrefix(player, Permissible.GROUP);
		
		prefix = (prefix != null) ? prefix : "";
		db.i("PermissionsHandler: Returning " + prefix);
		return prefix;
	}
	
	public String getGroupSuffix(Player player) {
		String suffix = "";
		db.i("PermissionsHandler: Getting group suffix of " + player.getName());
		
		int lastPriority = -1;
		
		for (String permission : groupAffixes.keySet()) {
			if (!has(player, permission))
				continue;
			
			Affix affix = groupAffixes.get(permission);
			
			int priority = affix.getPriority();
			
			if (priority > lastPriority) {
				suffix = affix.getSuffix();
				lastPriority = priority;
			}
		}
		
		if (suffix.isEmpty())
			suffix = permission.getSuffix(player, Permissible.GROUP);
		
		suffix = (suffix != null) ? suffix : "";
		db.i("PermissionsHandler: Returning " + suffix);
		return suffix;
	}
	
	private PermissionBridge loadPermissionBridge() {
		if (plugin.getConfig().getBoolean("use-vault", false) && packageExists("net.milkbowl.vault.Vault"))
			return new Bridge_Vault();
		
		if (packageExists("ru.tehkode.permissions.bukkit.PermissionsEx"))
			return new Bridge_PermissionsEx();
		
		if (packageExists("de.bananaco.bpermissions.imp.Permissions"))
			return new Bridge_bPermissions();
		
		if (packageExists("de.hydrox.bukkit.DroxPerms.DroxPerms"))
			return new Bridge_DroxPerms();
		
		if (packageExists("com.platymuus.bukkit.permissions.PermissionsPlugin"))
			return new Bridge_UltraPerms("PermissionBukkit");
		
		if (packageExists("net.crystalyx.bukkit.simplyperms.SimplyPlugin"))
			return new Bridge_UltraPerms("SimplyPerms");
		
		if (packageExists("org.anjocaido.groupmanager.GroupManager"))
			return new Bridge_GroupManager();
		
		if (packageExists("net.krinsoft.privileges.Privileges"))
			return new Bridge_UltraPerms("Privileges");
		
		if (packageExists("org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsPlugin"))
			return new Bridge_UltraPerms("zPermissions");
		
		return new Bridge_SuperPerms();
	}
	
	public String getPlayerPrefix(Player player) {
		String prefix = "";
		db.i("PermissionsHandler: Getting player prefix of " + player.getName());
		
		if (playersAffixes.containsKey(player.getName()))
			prefix = playersAffixes.get(player.getName()).getPrefix();
		
		if (prefix.isEmpty()) {
			for (String permission : playerAffixes.keySet()) {
				if (!has(player, permission))
					continue;
				
				prefix = playerAffixes.get(permission).getPrefix();
				break;
			}
		}
		
		if (prefix.isEmpty())
			prefix = permission.getPrefix(player, Permissible.PLAYER);
		
		if (prefix.isEmpty())
			prefix = getGroupPrefix(player);
		
		db.i("PermissionsHandler: Returning " + prefix);
		return prefix;
	}
	
	public String getPlayerSuffix(Player player) {
		String suffix = "";
		db.i("PermissionsHandler: Getting player suffix of " + player.getName());
		
		if (playersAffixes.containsKey(player.getName()))
			suffix = playersAffixes.get(player.getName()).getSuffix();
		
		if (suffix.isEmpty()) {
			for (String permission : playerAffixes.keySet()) {
				if (!has(player, permission))
					continue;
				
				suffix = playerAffixes.get(permission).getSuffix();
				break;
			}
		}
		
		if (suffix.isEmpty())
			suffix = getGroupPrefix(player);
		
		db.i("PermissionsHandler: Returning " + suffix);
		return suffix;
	}
	
	public boolean has(Player player, String permission) {
		try {
			return this.permission.hasPermission(player, permission);
		} catch (Exception e) {
			return player.hasPermission(permission);
		}
	}
	
	public void load() {
		PermissionDefault def = PermissionDefault.FALSE;
		
		ConfigurationSection groupSection = plugin.getConfig().getConfigurationSection("permissions.group");
		
		if (groupSection != null) {
			for (String group : groupSection.getKeys(false)) {
				String prefix = groupSection.getString(group + ".prefix", "");
				String suffix = groupSection.getString(group + ".suffix", "");
				int priority = groupSection.getInt(group + ".priority", 0);
				
				Permission permission = new Permission("TitanChat.affix." + group, def);
				plugin.getServer().getPluginManager().addPermission(permission);
				
				Affix affix = new Affix(permission, prefix, suffix, priority);
				groupAffixes.put(permission.getName(), affix);
			}
		}
		
		ConfigurationSection playerSection = plugin.getConfig().getConfigurationSection("permissions.player");
		
		if (playerSection != null) {
			for (String player : playerSection.getKeys(false)) {
				String prefix = playerSection.getString(player + ".prefix");
				String suffix = playerSection.getString(player + ".suffix");
				
				Permission permission = new Permission("TitanChat.affix." + player, def);
				plugin.getServer().getPluginManager().addPermission(permission);
				
				Affix affix = new Affix(permission, prefix, suffix, 0);
				playerAffixes.put(permission.getName(), affix);
			}
		}
		
		ConfigurationSection playersSection = plugin.getConfig().getConfigurationSection("permissions.players");
		
		if (playersSection != null) {
			for (String name : playersSection.getKeys(false)) {
				String prefix = playersSection.getString(name + ".prefix");
				String suffix = playersSection.getString(name + ".suffix");
				
				Affix affix = new Affix(new Permission(name), prefix, suffix, 0);
				playerAffixes.put(name, affix);
			}
		}
		
		this.permission = loadPermissionBridge();
		
		if (this.permission.getName().equals("SuperPerms"))
			plugin.log(Level.INFO, "SuperPerms loaded as backup permissions");
	}
	
	private boolean packageExists(String pkg) {
		try { Class.forName(pkg); return true; } catch (ClassNotFoundException e) { return false; }
	}
	
	public final class Affix {
		
		private final String prefix;
		private final String suffix;
		
		private final Permission permission;
		
		private final int priority;
		
		public Affix(Permission permission, String prefix, String suffix, int priority) {
			this.prefix = prefix;
			this.suffix = suffix;
			this.permission = permission;
			this.priority = priority;
		}
		
		public String getPermission() {
			return permission.getName();
		}
		
		public String getPrefix() {
			return prefix;
		}
		
		public int getPriority() {
			return priority;
		}
		
		public String getSuffix() {
			return suffix;
		}
	}
}