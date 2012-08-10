package com.titankingdoms.nodinchan.titanchat.permission.bridges;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.permission.PermissionBridge;

public final class Bridge_SuperPerms extends PermissionBridge {
	
	public Bridge_SuperPerms() {
		super("SuperPerms");
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
		return true;
	}
}