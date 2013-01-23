package com.titankingdoms.dev.titanchat.next.update;

import java.util.Map.Entry;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

public final class ConsoleParticipant extends Participant {
	
	private final PermissionAttachment permissions;
	
	public ConsoleParticipant() {
		super("CONSOLE");
		this.permissions = plugin.getServer().getConsoleSender().addAttachment(plugin);
	}
	
	@Override
	public void recalculatePermissions() {
		permissions.getPermissions().clear();
		
		for (Entry<Permission, Boolean> permission : getPermissions().entrySet())
			permissions.setPermission(permission.getKey(), permission.getValue());
		
		for (Entry<Permission, Boolean> permission : getChatGroup().getPermissions().entrySet())
			permissions.setPermission(permission.getKey(), permission.getValue());
	}
	
	@Override
	public void sendMessage(String message) {
		plugin.getServer().getConsoleSender().sendMessage(message);
	}
}