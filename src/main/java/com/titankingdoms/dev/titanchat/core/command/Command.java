/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
 *     
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.titankingdoms.dev.titanchat.core.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.help.HelpTopic;
import com.titankingdoms.dev.titanchat.loading.Loadable;
import com.titankingdoms.dev.titanchat.util.Messaging;

public abstract class Command extends Loadable {
	
	protected final TitanChat plugin;
	
	private String[] aliases;
	private String description;
	private String usage;
	private String permission;
	private int maxArgs;
	private int minArgs;
	
	public Command(String name) {
		super(name);
		this.plugin = TitanChat.getInstance();
		this.aliases = new String[0];
		this.description = "";
		this.usage = "";
		this.permission = "";
		this.maxArgs = 0;
		this.minArgs = 0;
	}
	
	protected final void broadcast(String... messages) {
		Messaging.broadcast(messages);
	}
	
	protected final void broadcast(Channel channel, String... messages) {
		Messaging.broadcast(channel, messages);
	}
	
	protected final void broadcast(World world, String... messages) {
		Messaging.broadcast(world, messages);
	}
	
	protected final void broadcast(CommandSender sender, double radius, String... messages) {
		Messaging.broadcast(sender, radius, messages);
	}
	
	public abstract void execute(CommandSender sender, Channel channel, String[] args);
	
	public String[] getAliases() {
		return aliases;
	}
	
	protected final ConsoleCommandSender getConsoleSender() {
		return plugin.getServer().getConsoleSender();
	}
	
	public String getDescription() {
		return description;
	}
	
	protected final String getDisplay(OfflinePlayer player) {
		return "";
	}
	
	public int getMaxArguments() {
		return maxArgs;
	}
	
	public int getMinArguments() {
		return minArgs;
	}
	
	protected final OfflinePlayer getOfflinePlayer(String name) {
		return plugin.getServer().getOfflinePlayer(name);
	}
	
	public String getPermission() {
		return permission;
	}
	
	protected final Player getPlayer(String name) {
		return plugin.getServer().getPlayer(name);
	}
	
	public String getUsage() {
		return usage;
	}
	
	protected final boolean hasPermission(CommandSender sender, String permission) {
		return (sender instanceof ConsoleCommandSender) ? true : sender.hasPermission(permission);
	}
	
	protected final boolean isOnline(String name) {
		return getOfflinePlayer(name).isOnline();
	}
	
	protected final void msg(CommandSender sender, String... messages) {
		Messaging.sendMessage(sender, messages);
	}
	
	protected final void msg(OfflinePlayer player, String... messages) {
		if (player.isOnline())
			player.getPlayer().sendMessage(messages);
	}
	
	public abstract boolean permissionCheck(CommandSender sender, Channel channel);
	
	protected final void registerHelpTopic(HelpTopic topic) {
		
	}
	
	protected void setAliases(String... aliases) {
		this.aliases = aliases;
	}
	
	protected void setArgumentRange(int minimum, int maximum) {
		this.maxArgs = maximum;
		this.minArgs = minimum;
	}
	
	protected void setDescription(String description) {
		this.description = description;
	}
	
	protected void setPermission(String permission) {
		this.permission = permission;
	}
	
	protected void setUsage(String usage) {
		this.usage = usage;
	}
}