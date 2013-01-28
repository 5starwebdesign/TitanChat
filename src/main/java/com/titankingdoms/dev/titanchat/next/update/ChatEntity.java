package com.titankingdoms.dev.titanchat.next.update;

import java.util.Map;

import org.bukkit.permissions.Permission;

import com.titankingdoms.dev.titanchat.loading.Loadable;

public abstract class ChatEntity extends Loadable {
	
	public ChatEntity(String name) {
		super(name);
	}
	
	public abstract String getEntityType();
	
	public abstract Map<String, Boolean> getPermissions();
	
	public abstract void recalculatePermissions();
	
	public abstract void sendMessage(String message);
	
	public void sendMessage(String... messages) {
		for (String message : messages)
			sendMessage(message);
	}
	
	public abstract void setPermission(Permission permission, boolean value);
	
	public abstract void setPermission(String node, boolean value);
	
	public abstract void unsetPermission(Permission permission);
	
	public abstract void unsetPermission(String node);
}