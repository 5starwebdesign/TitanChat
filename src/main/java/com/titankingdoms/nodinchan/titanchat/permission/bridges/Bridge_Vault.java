package com.titankingdoms.nodinchan.titanchat.permission.bridges;

import org.bukkit.entity.Player;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import com.titankingdoms.nodinchan.titanchat.permission.PermissionBridge;

public final class Bridge_Vault extends PermissionBridge {
	
	private Permission perm;
	private Chat chat;
	
	public Bridge_Vault() {
		super("Vault");
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
}