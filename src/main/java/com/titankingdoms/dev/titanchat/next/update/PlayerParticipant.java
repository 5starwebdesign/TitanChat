package com.titankingdoms.dev.titanchat.next.update;

import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

public final class PlayerParticipant extends Participant {
	
	private final PermissionAttachment permissions;
	
	public PlayerParticipant(String name) {
		super(name);
		this.permissions = null;
	}
	
	public PlayerParticipant(Player player) {
		super(player.getName());
		this.permissions = player.addAttachment(plugin);
	}

	@Override
	public void recalculatePermissions() {
		if (permissions == null)
			return;
		
		permissions.getPermissions().clear();
		
		for (Entry<Permission, Boolean> permission : getPermissions().entrySet())
			permissions.setPermission(permission.getKey(), permission.getValue());
		
		for (Entry<Permission, Boolean> permission : getChatGroup().getPermissions().entrySet())
			permissions.setPermission(permission.getKey(), permission.getValue());
	}

	@Override
	public void sendMessage(String message) {
		
	}
}