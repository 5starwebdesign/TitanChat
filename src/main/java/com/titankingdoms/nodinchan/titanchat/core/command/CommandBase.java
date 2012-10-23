package com.titankingdoms.nodinchan.titanchat.core.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.loading.Loadable;

public class CommandBase extends Loadable {
	
	public CommandBase(String name) {
		super(name);
	}
	
	public ConsoleCommandSender getConsoleSender() {
		return plugin.getServer().getConsoleSender();
	}
	
	public OfflinePlayer getOfflinePlayer(String name) {
		return plugin.getServer().getOfflinePlayer(name);
	}
	
	public Player getPlayer(String name) {
		return plugin.getServer().getPlayer(name);
	}
	
	public Player getPlayerExact(String name) {
		return plugin.getServer().getPlayerExact(name);
	}
}