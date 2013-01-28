package com.titankingdoms.dev.titanchat.next.update;

import java.util.HashMap;
import java.util.Map;

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
	public Map<String, Boolean> getPermissions() {
		if (permissions == null)
			return new HashMap<String, Boolean>();
		
		return new HashMap<String, Boolean>(permissions.getPermissions());
	}

	@Override
	public void recalculatePermissions() {
		if (permissions == null)
			return;
		
		for (String node : getPermissions().keySet())
			if (node.startsWith("TitanChat.group."))
				unsetPermission(node);
		
		if (getCurrentChannel() != null)
			setPermission("TitanChat.group." + getChatGroup(getCurrentChannel()).getName(), true);
	}

	@Override
	public void sendMessage(String message) {
		
	}
	
	@Override
	public void setPermission(Permission permission, boolean value) {
		if (permissions == null)
			return;
		
		permissions.setPermission(permission, value);
	}
	
	@Override
	public void setPermission(String node, boolean value) {
		if (permissions == null)
			return;
		
		permissions.setPermission(node, value);
	}
	
	@Override
	public void unsetPermission(Permission permission) {
		if (permissions == null)
			return;
		
		permissions.unsetPermission(permission);
	}
	
	@Override
	public void unsetPermission(String node) {
		if (permissions == null)
			return;
		
		permissions.unsetPermission(node);
	}
}