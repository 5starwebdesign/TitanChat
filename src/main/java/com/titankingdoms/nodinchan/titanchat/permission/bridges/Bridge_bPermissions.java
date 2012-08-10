package com.titankingdoms.nodinchan.titanchat.permission.bridges;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.titankingdoms.nodinchan.titanchat.permission.PermissionBridge;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;

public final class Bridge_bPermissions extends PermissionBridge {
	
	private static final CalculableType GROUP = CalculableType.GROUP;
	private static final CalculableType USER = CalculableType.USER;
	
	private boolean hooked = false;
	
	public Bridge_bPermissions() {
		super("bPermissions");
		
		Plugin perm = plugin.getServer().getPluginManager().getPlugin("bPermissions");
		
		if (perm != null && perm.isEnabled()) {
			hooked = true;
			log(Level.INFO, getName() + " hooked");
		}
		
		plugin.getServer().getPluginManager().registerEvents(new ServerListener(new bPermissionsChecker()), plugin);
	}
	
	@Override
	public String getPrefix(Player player, Permissible permissible) {
		if (permissible == null)
			return "";
		
		switch (permissible) {
		
		case GROUP:
			String[] groups = ApiLayer.getGroups(player.getWorld().getName(), USER, player.getName());
			return (groups.length > 0) ? ApiLayer.getValue(player.getWorld().getName(), GROUP, groups[0], "prefix") : "";
			
		case PLAYER:
			return ApiLayer.getValue(player.getWorld().getName(), USER, player.getName(), "prefix");
		}
		
		return "";
	}
	
	@Override
	public String getSuffix(Player player, Permissible permissible) {
		if (permissible == null)
			return "";
		
		switch (permissible) {
		
		case GROUP:
			String[] groups = ApiLayer.getGroups(player.getWorld().getName(), USER, player.getName());
			return (groups.length > 0) ? ApiLayer.getValue(player.getWorld().getName(), GROUP, groups[0], "suffix") : "";
			
		case PLAYER:
			return ApiLayer.getValue(player.getWorld().getName(), USER, player.getName(), "suffix");
		}
		
		return "";
	}
	
	@Override
	public boolean hasPermission(Player player, String permission) {
		return ApiLayer.hasPermission(player.getWorld().getName(), USER, player.getName(), permission);
	}
	
	@Override
	public boolean isEnabled() {
		return hooked;
	}
	
	public final class bPermissionsChecker extends PluginChecker {
		
		@Override
		public void onPluginDisable(Plugin plugin) {
			if (hooked) {
				if (plugin.getName().equals("bPermissions")) {
					hooked = false;
					log(Level.INFO, getName() + " unhooked");
				}
			}
		}
		
		@Override
		public void onPluginEnable(Plugin plugin) {
			if (!hooked) {
				Plugin perm = plugin.getServer().getPluginManager().getPlugin("bPermissions");
				
				if (perm != null && perm.isEnabled()) {
					hooked = true;
					log(Level.INFO, getName() + " hooked");
				}
			}
		}
	}
}