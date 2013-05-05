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
import com.titankingdoms.dev.titanchat.format.tag.Tag;
import com.titankingdoms.dev.titanchat.loading.Loadable;
import com.titankingdoms.dev.titanchat.util.Messaging;

/**
 * {@link Command} - Commands of TitanChat
 * 
 * @author NodinChan
 *
 */
public abstract class Command extends Loadable {
	
	private String[] aliases;
	private String description;
	private String usage;
	private int maxArgs;
	private int minArgs;
	
	public Command(String name) {
		super(name);
		this.aliases = new String[0];
		this.description = "";
		this.usage = "";
		this.maxArgs = 0;
		this.minArgs = 0;
	}
	
	/**
	 * Broadcasts the messages globally
	 * 
	 * @param messages The messages the broadcast
	 */
	public final void broadcast(String... messages) {
		Messaging.broadcast(messages);
	}
	
	/**
	 * Broadcasts the messages in the {@link Channel}
	 * 
	 * @param channel The {@link Channel} to broadcast in
	 * 
	 * @param messages The messages to broadcast
	 */
	public final void broadcast(Channel channel, String... messages) {
		Messaging.broadcast(channel, messages);
	}
	
	/**
	 * Broadcasts the messages in the {@link World}
	 * 
	 * @param world The {@link World} to broadcast in
	 * 
	 * @param messages The messages to broadcast
	 */
	public final void broadcast(World world, String... messages) {
		Messaging.broadcast(world, messages);
	}
	
	/**
	 * Broadcasts the messages to {@link Player}s around the sender
	 * 
	 * @param sender The sender of the message
	 * 
	 * @param radius The radius around the sender
	 * 
	 * @param messages The messages to broadcast
	 */
	public final void broadcast(CommandSender sender, double radius, String... messages) {
		Messaging.broadcast(sender, radius, messages);
	}
	
	/**
	 * Executes the {@link Command}
	 * 
	 * @param sender The {@link CommandSender}
	 * 
	 * @param channel The targetted {@link Channel}
	 * 
	 * @param args The arguments of the command
	 */
	public abstract void execute(CommandSender sender, Channel channel, String[] args);
	
	/**
	 * Gets the aliases of the {@link Command}
	 * 
	 * @return The aliases
	 */
	public String[] getAliases() {
		return aliases;
	}
	
	/**
	 * Gets the {@link ConsoleCommandSender}
	 * 
	 * @return The {@link ConsoleCommandSender}
	 */
	protected final ConsoleCommandSender getConsoleSender() {
		return plugin.getServer().getConsoleSender();
	}
	
	/**
	 * Gets the description of the {@link Command}
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the maximum amount of arguments
	 * 
	 * @return The maximum amount
	 */
	public int getMaxArguments() {
		return maxArgs;
	}
	
	/**
	 * Gets the minimum amount of arguments
	 * 
	 * @return The minimum amount
	 */
	public int getMinArguments() {
		return minArgs;
	}
	
	/**
	 * Gets the usage
	 * 
	 * @return The usage
	 */
	public String getUsage() {
		return usage;
	}
	
	/**
	 * Checks if the {@link CommandSender} has permission to use the {@link Command}
	 * 
	 * @param sender The {@link CommandSender}
	 * 
	 * @param channel The targetted {@link Channel}
	 * 
	 * @return True if the {@link CommandSender} has permission
	 */
	public abstract boolean permissionCheck(CommandSender sender, Channel channel);
	
	/**
	 * Registers the {@link ChatAddon}s
	 * 
	 * @param addons The {@link ChatAddon}s to register
	 */
	public final void register(ChatAddon... addons) {
		plugin.getAddonManager().registerAddons(addons);
	}
	
	/**
	 * Registers the {@link Channel}s
	 * 
	 * @param channels The {@link Channel}s to register
	 */
	public final void register(Channel... channels) {
		plugin.getChannelManager().registerChannels(channels);
	}
	
	/**
	 * Registers the {@link ChannelLoader}s
	 * 
	 * @param loaders The {@link ChannelLoader}s to register
	 */
	public final void register(ChannelLoader... loaders) {
		plugin.getChannelManager().registerLoaders(loaders);
	}
	
	/**
	 * Registers the {@link Command}s
	 * 
	 * @param commands The {@link Command}s to register
	 */
	public final void register(Command... commands) {
		plugin.getCommandManager().registerCommands(commands);
	}
	
	/**
	 * Registers the {@link Tag}s
	 * 
	 * @param tags The {@link Tag}s to register
	 */
	public final void register(Tag... tags) {
		plugin.getTagManager().registerTags(tags);
	}
	
	/**
	 * Sends the messages to the {@link CommandSender}
	 * 
	 * @param sender The {@link CommandSender} to send to
	 * 
	 * @param messages The messages to send
	 */
	public final void sendMessage(CommandSender sender, String... messages) {
		Messaging.sendMessage(sender, messages);
	}
	
	/**
	 * Sets the aliases of the {@link Command}
	 * 
	 * @param aliases The new aliases
	 */
	protected void setAliases(String... aliases) {
		this.aliases = (aliases != null) ? aliases : new String[0];
	}
	
	/**
	 * Sets the argument range of the {@link Command}
	 * 
	 * @param minimum The new minimum amount of arguments
	 * 
	 * @param maximum The new maximum amount of arguments
	 */
	protected void setArgumentRange(int minimum, int maximum) {
		this.maxArgs = (maximum >= 0) ? maximum : 0;
		this.minArgs = (minimum >= 0) ? minimum : 0;
	}
	
	/**
	 * Sets the description of the {@link Command}
	 * 
	 * @param description The new description
	 */
	protected void setDescription(String description) {
		this.description = (description != null) ? description : "";
	}
	
	/**
	 * Sets the usage of the {@link Command}
	 * 
	 * @param usage The new usage
	 */
	protected void setUsage(String usage) {
		this.usage = (usage != null) ? usage : "";
	}
}