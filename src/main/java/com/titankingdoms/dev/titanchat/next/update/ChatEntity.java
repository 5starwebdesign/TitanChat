package com.titankingdoms.dev.titanchat.next.update;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.permissions.Permission;

import com.titankingdoms.dev.titanchat.loading.Loadable;

public abstract class ChatEntity extends Loadable {
	
	private final Map<Permission, Boolean> permissions = new HashMap<Permission, Boolean>();
	
	public ChatEntity(String name) {
		super(name);
	}
	
	public abstract String getEntityType();
	
	public final Map<Permission, Boolean> getPermissions() {
		return new HashMap<Permission, Boolean>(permissions);
	}
	
	public abstract void recalculatePermissions();
	
	public abstract void sendMessage(String message);
	
	public void sendMessage(String... messages) {
		for (String message : messages)
			sendMessage(message);
	}
	
	public final void setPermission(Permission permission, boolean value) {
		this.permissions.put(permission, value);
	}
	
	public final void setPermission(String node, boolean value) {
		setPermission(new Permission(node), value);
	}
	
	public final void unsetPermission(Permission permission) {
		this.permissions.remove(permission);
	}
	
	public final void unsetPermission(String node) {
		unsetPermission(new Permission(node));
	}
}