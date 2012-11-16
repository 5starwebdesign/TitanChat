package com.titankingdoms.nodinchan.titanchat.permissions;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;
import de.hydrox.bukkit.DroxPerms.DroxPerms;
import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;

public final class SimplePermissionsBridge {
	
	private final TitanChat plugin;
	
	private final PluginManager pm;
	
	public SimplePermissionsBridge() {
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
		
		if ((prefix == null || prefix.isEmpty()) && pm.getPlugin("bPermissions") != null)
			prefix = ApiLayer.getValue(player.getWorld().getName(), CalculableType.USER, player.getName(), "prefix");
		
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
		
		if ((suffix == null || suffix.isEmpty()) && pm.getPlugin("bPermissions") != null)
			suffix = ApiLayer.getValue(player.getWorld().getName(), CalculableType.USER, player.getName(), "suffix");
		
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
	
	public boolean hasPermission(CommandSender sender, String permission) {
		return (sender instanceof Player) ? hasPermission((Player) sender, permission) : true;
	}
	
	public boolean hasPermission(Player player, String permission) {
		if (pm.getPlugin("Vault") != null)
			return plugin.getServer().getServicesManager().load(Permission.class).playerHas(player, permission);
		
		if (pm.getPlugin("bPermissions") != null)
			return ApiLayer.hasPermission(player.getWorld().getName(), CalculableType.USER, player.getName(), permission);
		
		if (pm.getPlugin("PermissionsEx") != null)
			return PermissionsEx.getPermissionManager().has(player, permission);
		
		if (pm.getPlugin("GroupManager") != null)
			return ((GroupManager) pm.getPlugin("GroupManager")).getWorldsHolder().getWorldPermissions(player).has(player, permission);
		
		return player.hasPermission(permission);
	}
}