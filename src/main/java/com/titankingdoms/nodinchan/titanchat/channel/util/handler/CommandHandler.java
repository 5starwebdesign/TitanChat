/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat.channel.util.handler;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.util.handler.Handler.HandlerInfo;
import com.titankingdoms.nodinchan.titanchat.command.Executor;

//TODO: Remove the package and write something better

/**
 * CommandHandler - Abstract command handler for command overriding in channels
 * 
 * @author NodinChan
 *
 */
public abstract class CommandHandler {
	
	protected final TitanChat plugin;
	
	protected final Channel channel;
	
	private final String command;
	
	private final HandlerInfo info;
	
	public CommandHandler(Channel channel, String command, HandlerInfo info) {
		this.plugin = TitanChat.getInstance();
		this.channel = channel;
		this.command = command;
		this.info = info;
	}
	
	/**
	 * Gets the command to override
	 * 
	 * @return The command to override
	 */
	public final String getCommand() {
		return command;
	}
	
	/**
	 * Gets the handler info
	 * 
	 * @return The info of the handler
	 */
	public final HandlerInfo getInfo() {
		return info;
	}
	
	/**
	 * Checks if the sender has the specified permission
	 * 
	 * @param sender The sender to check
	 * 
	 * @param permission The permission to check with
	 * 
	 * @return True if the sender has permission
	 */
	public final boolean hasPermission(CommandSender sender, String permission) {
		if (!(sender instanceof Player))
			return true;
		
		return sender.hasPermission(permission);
	}
	
	/**
	 * Informs the command sender that the argument length is invalid
	 * 
	 * @param sender The sender to inform
	 */
	public final void invalidArgLength(CommandSender sender) {
		plugin.send(MessageLevel.WARNING, sender, "Invalid Argument Length");
		usage(sender);
	}
	
	/**
	 * When the command is used
	 * 
	 * @param sender The command sender
	 * 
	 * @param args The arguments
	 */
	public abstract void onCommand(CommandSender sender, String[] args);
	
	/**
	 * Sends the usage of the command
	 * 
	 * @param sender The sender to send to
	 */
	public final void usage(CommandSender sender) {
		if (info.getUsage().isEmpty()) {
			Executor executor = plugin.getManager().getCommandManager().getCommandExecutor(command);
			
			if (executor != null && !executor.getUsage().isEmpty())
				plugin.send(MessageLevel.WARNING, sender, "Usage: /titanchat <@><channel> " + executor.getUsage());
			
		} else { plugin.send(MessageLevel.WARNING, sender, "Usage: /titanchat <@><channel> " + info.getUsage()); }
	}
}