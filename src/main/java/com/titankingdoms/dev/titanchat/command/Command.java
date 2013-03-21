/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat.command;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.titankingdoms.dev.titanchat.addon.ChatAddon;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.dev.titanchat.loading.Loadable;
import com.titankingdoms.dev.titanchat.util.Messaging;

public abstract class Command extends Loadable {
	
	private String[] aliases;
	private String usage;
	private String permission;
	private int maxArgs;
	private int minArgs;
	
	public Command(String name) {
		super(name);
		this.aliases = new String[0];
		this.usage = "";
		this.permission = "";
		this.maxArgs = 0;
		this.minArgs = 0;
	}
	
	public final void broadcast(String... messages) {
		Messaging.broadcast(messages);
	}
	
	public final void broadcast(Channel channel, String... messages) {
		Messaging.broadcast(channel, messages);
	}
	
	public final void broadcast(World world, String... messages) {
		Messaging.broadcast(world, messages);
	}
	
	public final void broadcast(CommandSender sender, double radius, String... messages) {
		Messaging.broadcast(sender, radius, messages);
	}
	
	public abstract void execute(CommandSender sender, Channel channel, String[] args);
	
	public String[] getAliases() {
		return aliases;
	}
	
	protected final ConsoleCommandSender getConsoleSender() {
		return plugin.getServer().getConsoleSender();
	}
	
	public int getMaxArguments() {
		return maxArgs;
	}
	
	public int getMinArguments() {
		return minArgs;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public abstract boolean permissionCheck(CommandSender sender, Channel channel);
	
	public final void register(ChatAddon... addons) {
		plugin.getAddonManager().registerAddons(addons);
	}
	
	public final void register(Channel... channels) {
		plugin.getChannelManager().registerChannels(channels);
	}
	
	public final void register(ChannelLoader... loaders) {
		plugin.getChannelManager().registerLoaders(loaders);
	}
	
	public final void register(Command... commands) {
		plugin.getCommandManager().registerCommands(commands);
	}
	
	public final void sendMessage(CommandSender sender, String... messages) {
		Messaging.sendMessage(sender, messages);
	}
	
	protected void setAliases(String... aliases) {
		this.aliases = aliases;
	}
	
	protected void setArgumentRange(int minimum, int maximum) {
		this.maxArgs = maximum;
		this.minArgs = minimum;
	}
	
	protected void setPermission(String permission) {
		this.permission = permission;
	}
	
	protected void setUsage(String usage) {
		this.usage = usage;
	}
}