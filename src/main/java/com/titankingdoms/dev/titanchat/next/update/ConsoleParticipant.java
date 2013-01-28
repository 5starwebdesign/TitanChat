package com.titankingdoms.dev.titanchat.next.update;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

public final class ConsoleParticipant extends Participant {
	
	private final PermissionAttachment permissions;
	
	public ConsoleParticipant() {
		super("CONSOLE");
		this.permissions = plugin.getServer().getConsoleSender().addAttachment(plugin);
	}
	
	@Override
	public Map<String, Boolean> getPermissions() {
		return new HashMap<String, Boolean>(permissions.getPermissions());
	}
	
	@Override
	public boolean equals(Object object) {
		return this == object;
	}
	
	@Override
	public void recalculatePermissions() {
		for (String node : getPermissions().keySet())
			if (node.startsWith("TitanChat.group."))
				unsetPermission(node);
		
		if (getCurrentChannel() != null)
			setPermission("TitanChat.group." + getChatGroup(getCurrentChannel()).getName(), true);
	}
	
	@Override
	public void sendMessage(String message) {
		plugin.getServer().getConsoleSender().sendMessage(message);
	}
	
	@Override
	public void setPermission(Permission permission, boolean value) {
		permissions.setPermission(permission, value);
	}
	
	@Override
	public void setPermission(String node, boolean value) {
		permissions.setPermission(node, value);
	}
	
	@Override
	public void unsetPermission(Permission permission) {
		permissions.unsetPermission(permission);
	}
	
	@Override
	public void unsetPermission(String node) {
		permissions.unsetPermission(node);
	}
}