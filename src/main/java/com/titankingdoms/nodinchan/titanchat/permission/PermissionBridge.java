package com.titankingdoms.nodinchan.titanchat.permission;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

public abstract class PermissionBridge {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	public PermissionBridge(String name) {
		this.plugin = TitanChat.getInstance();
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}
	
	public abstract String getPrefix(Player player, Permissible permissible);
	
	public abstract String getSuffix(Player player, Permissible permissible);
	
	public boolean has(CommandSender sender, String permission) {
		return sender.hasPermission(permission);
	}
	
	public boolean has(Player player, String permission) {
		return player.hasPermission(permission);
	}
	
	public abstract boolean hasPermission(Player player, String permission);
	
	public abstract boolean isEnabled();
	
	public final void log(Level level, String msg) {
		plugin.log(level, msg);
	}
	
	public enum Permissible { GROUP, PLAYER }
	
	public final class ServerListener implements Listener {
		
		private final PluginChecker checker;
		
		public ServerListener(PluginChecker checker) {
			this.checker = checker;
		}
		
		@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
		public void onPluginDisable(PluginDisableEvent event) {
			checker.onPluginDisable(event.getPlugin());
		}
		
		@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
		public void onPluginEnable(PluginEnableEvent event) {
			checker.onPluginEnable(event.getPlugin());
		}
	}
	
	public abstract class PluginChecker {
		
		public abstract void onPluginDisable(Plugin plugin);
		
		public abstract void onPluginEnable(Plugin plugin);
	}
}