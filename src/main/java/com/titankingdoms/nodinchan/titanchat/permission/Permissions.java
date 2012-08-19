package com.titankingdoms.nodinchan.titanchat.permission;

import java.util.List;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;

public final class Permissions {
	
	private final PluginManager pm;
	
	protected final PermissionDefault ALL = PermissionDefault.TRUE;
	protected final PermissionDefault OPS = PermissionDefault.OP;
	protected final PermissionDefault NOTOP = PermissionDefault.NOT_OP;
	protected final PermissionDefault NONE = PermissionDefault.FALSE;
	
	public Permissions() {
		this.pm = TitanChat.getInstance().getServer().getPluginManager();
	}
	
	/**
	 * Loads channel specific permissions
	 * 
	 * @param channel The channel to load for
	 */
	public void load(Channel channel) {
		String name = channel.getName();
		
		Permission autojoin = new Permission("TitanChat.autojoin." + name, "Autojoins " + name + " on login", NONE);
		pm.addPermission(autojoin);
		autojoin.addParent("TitanChat.autojoin.*", true);
		
		Permission autoleave = new Permission("TitanChat.autoleave." + name, "Autoleaves " + name + " on login", NONE);
		pm.addPermission(autoleave);
		autoleave.addParent("TitanChat.autoleave.*", true);
		
		Permission spawn = new Permission("TitanChat.spawn." + name, "Sets " + name + " as spawn", NONE);
		pm.addPermission(spawn);
		
		Permission bypass = new Permission("TitanChat.bypass." + name, "Grants several permissions for bypassing " + name);
		pm.addPermission(bypass);
		bypass.addParent("TitanChat.bypass.*", true);
		
		Permission join = new Permission("TitanChat.join." + name, "Grants permission to join " + name);
		pm.addPermission(join);
		join.addParent("TitanChat.join.*", true);
		join.addParent(bypass, true);
		
		Permission leave = new Permission("TitanChat.leave." + name, "Grants permission to leave " + name);
		pm.addPermission(leave);
		leave.addParent("TitanChat.leave.*", true);
		leave.addParent(bypass, true);
		
		Permission voice = new Permission("TitanChat.voice." + name, "Grants permission to speak at all times in " + name);
		pm.addPermission(voice);
		voice.addParent("TitanChat.voice.*", true);
		voice.addParent(bypass, true);
		
		Permission speak = new Permission("TitanChat.speak." + name, "Grants permission to speak in " + name);
		pm.addPermission(speak);
		speak.addParent("TitanChat.speak.*", true);
		speak.addParent(voice, true);
		
		Permission ban = new Permission("TitanChat.ban." + name, "Grants permission to ban in " + name);
		pm.addPermission(ban);
		ban.addParent("TitanChat.ban.*", true);
		
		Permission kick = new Permission("TitanChat.kick." + name, "Grants permission to kick in " + name);
		pm.addPermission(kick);
		kick.addParent("TitanChat.kick.*", true);
		
		Permission mute = new Permission("TitanChat.mute." + name, "Grants permission to mute in " + name);
		pm.addPermission(mute);
		mute.addParent("TitanChat.mute.*", true);
		
		Permission rank = new Permission("TitanChat.rank." + name, "Grants permission to rank in " + name);
		pm.addPermission(rank);
		rank.addParent("TitanChat.rank.*", true);
		
		Permission emote = new Permission("TitanChat.emote." + name, "Grants permission to the emote command in " + name);
		pm.addPermission(emote);
		emote.addParent("TitanChat.emote.*", true);
	}
	
	/**
	 * Loads channel specific permissions
	 * 
	 * @param channels The channels to load for
	 */
	public void load(List<Channel> channels) {
		for (Channel channel : channels)
			load(channel);
	}
	
	/**
	 * Unloads channel specific permissions
	 * 
	 * @param channel The channel to unload for
	 */
	public void unload(Channel channel) {
		String name = channel.getName();
		
		pm.removePermission("TitanChat.autojoin." + name);
		pm.removePermission("TitanChat.autoleave." + name);
		pm.removePermission("TitanChat.spawn." + name);
		pm.removePermission("TitanChat.bypass." + name);
		pm.removePermission("TitanChat.join." + name);
		pm.removePermission("TitanChat.leave." + name);
		pm.removePermission("TitanChat.voice." + name);
		pm.removePermission("TitanChat.speak." + name);
		pm.removePermission("TitanChat.ban." + name);
		pm.removePermission("TitanChat.kick." + name);
		pm.removePermission("TitanChat.mute." + name);
		pm.removePermission("TitanChat.rank." + name);
		pm.removePermission("TitanChat.emote." + name);
	}
	
	/**
	 * Unloads channel specific permissions
	 * 
	 * @param channels The channels to unload for
	 */
	public void unload(List<Channel> channels) {
		for (Channel channel : channels)
			unload(channel);
	}
}