package com.titankingdoms.nodinchan.titanchat.permission.bridges;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

import com.titankingdoms.nodinchan.titanchat.permission.PermissionBridge;

public final class Bridge_zPermissions extends PermissionBridge {
	
	private ZPermissionsService service;
	
	public Bridge_zPermissions() {
		super("zPermissions");
		
		service = plugin.getServer().getServicesManager().load(ZPermissionsService.class);
		
		if (service != null)
			log(Level.INFO, getName() + " hooked");
		
		plugin.getServer().getPluginManager().registerEvents(new ServerListener(new zPermissionsChecker()), plugin);
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
		return service != null;
	}
	
	public final class zPermissionsChecker extends PluginChecker {
		
		@Override
		public void onPluginDisable(Plugin plugin) {
			if (service != null) {
				if (plugin.getName().equals("zPermissions")) {
					service = null;
					log(Level.INFO, getName() + " unhooked");
				}
			}
		}
		
		@Override
		public void onPluginEnable(Plugin plugin) {
			if (service == null) {
				service = plugin.getServer().getServicesManager().load(ZPermissionsService.class);
				
				if (service != null)
					log(Level.INFO, getName() + " hooked");
			}
		}
	}
}