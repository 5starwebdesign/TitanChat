package com.titankingdoms.dev.titanchat.next.update;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.permissions.Permission;

public class ChatGroup extends ChatEntity {
	
	private final Permission permission;
	
	public ChatGroup(String name) {
		super(name);
		this.permission = new Permission("TitanChat.group." + name);
		plugin.getServer().getPluginManager().addPermission(permission);
	}
	
	@Override
	public String getEntityType() {
		return "ChatGroup";
	}
	
	@Override
	public Map<String, Boolean> getPermissions() {
		return new HashMap<String, Boolean>(permission.getChildren());
	}
	
	@Override
	public void recalculatePermissions() {
		
	}
	
	@Override
	public void sendMessage(String message) {
		
	}
	
	@Override
	public void setPermission(Permission permission, boolean value) {
		permission.addParent(this.permission, value);
		plugin.getServer().getPluginManager().addPermission(permission);
	}
	
	@Override
	public void setPermission(String node, boolean value) {
		setPermission(new Permission(node), value);
	}
	
	@Override
	public void unsetPermission(Permission permission) {
		unsetPermission(permission.getName());
	}
	
	@Override
	public void unsetPermission(String node) {
		this.permission.getChildren().remove(node);
	}
}